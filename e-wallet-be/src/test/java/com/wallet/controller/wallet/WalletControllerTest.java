package com.wallet.controller.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wallet.controller.AbstractControllerTest;
import com.wallet.dto.WalletDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest extends AbstractControllerTest {

    @Test
    void unauthorisedAccessToWalletApi() throws Exception {
        String walletId = "walletId";
        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWallets() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_get_wallets");

        //create wallets
        BigDecimal initialBalance = BigDecimal.ZERO.setScale(2, RoundingMode.UP);
        List<WalletDto> expectedWallets = new LinkedList<>();
        expectedWallets.add(
                createWallet(
                        token,
                        new WalletDto(null, "Wallet name 1", "Wallet description 1", initialBalance)
                )
        );
        expectedWallets.add(
                createWallet(
                        token,
                        new WalletDto(null, "Wallet name 2", "Wallet description 2", initialBalance)
                )
        );

        List<WalletDto> wallets = mapper.readValue(
                mockMvc.perform(get("/api/v1/wallet")
                                .header(HttpHeaders.AUTHORIZATION, token)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), new TypeReference<>() {
                });

        assertEquals(expectedWallets, wallets);
    }

    @Test
    void topupWallet() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_topup");

        //create wallet
        BigDecimal initialBalance = BigDecimal.ZERO;
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );
        assertEquals(initialBalance, wallet.balance());

        //topup wallet
        String walletId = wallet.id();
        BigDecimal amount = BigDecimal.TEN;
        WalletDto replenishedWallet = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/wallet/{walletId}/topup/{amount}", walletId, amount)
                                .header(HttpHeaders.AUTHORIZATION, token)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), WalletDto.class);

        assertEquals(replenishedWallet.id(), wallet.id());
        assertEquals(replenishedWallet.name(), wallet.name());
        assertEquals(replenishedWallet.description(), wallet.description());
        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.UP), replenishedWallet.balance());

        //check balance
        mockMvc.perform(get("/api/v1/wallet/{walletId}/balance", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().json(mapper.writeValueAsString(BigDecimal.TEN.setScale(2, RoundingMode.UP)))
                );

    }

    @Test
    void withdrawWallet() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_withdraw");

        //create wallet
        BigDecimal initialBalance = BigDecimal.TEN;
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );

        assertEquals(initialBalance, wallet.balance());

        //withdraw wallet
        String walletId = wallet.id();
        BigDecimal amount = BigDecimal.ONE;
        BigDecimal expectedBalance = initialBalance.subtract(amount).setScale(2, RoundingMode.UP);
        WalletDto replenishedWallet = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/wallet/{walletId}/withdraw/{amount}", walletId, amount)
                                .header(HttpHeaders.AUTHORIZATION, token)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), WalletDto.class);

        assertEquals(replenishedWallet.id(), wallet.id());
        assertEquals(replenishedWallet.name(), wallet.name());
        assertEquals(replenishedWallet.description(), wallet.description());
        assertEquals(expectedBalance, replenishedWallet.balance());

    }

    @Test
    void transfer() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_transfer");

        //create from wallet
        BigDecimal initialBalanceFrom = BigDecimal.TEN;
        WalletDto fromWallet = createWallet(
                token,
                new WalletDto(null, "Wallet from name", "Wallet from description", initialBalanceFrom)
        );
        assertEquals(initialBalanceFrom, fromWallet.balance());

        BigDecimal initialBalanceTo = BigDecimal.ZERO;
        WalletDto toWallet = createWallet(
                token,
                new WalletDto(null, "Wallet from name", "Wallet from description", initialBalanceTo)
        );
        assertEquals(initialBalanceTo, toWallet.balance());

        //withdraw wallet
        String fromWalletId = fromWallet.id();
        String toWalletId = toWallet.id();
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.UP);
        WalletDto afterTransferWallet = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders
                                        .patch("/api/v1/wallet/{fromWalletId}/transfer/{toWalletId}/{amount}", fromWalletId, toWalletId, amount)
                                        .header(HttpHeaders.AUTHORIZATION, token)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), WalletDto.class);

        assertEquals(fromWallet.id(), afterTransferWallet.id());
        assertEquals(fromWallet.name(), afterTransferWallet.name());
        assertEquals(fromWallet.description(), afterTransferWallet.description());
        BigDecimal expectedBalance = initialBalanceFrom.subtract(amount).setScale(2, RoundingMode.UP);
        assertEquals(expectedBalance, afterTransferWallet.balance());

        WalletDto replenishedToWallet = getWallet(token, toWalletId);

        assertEquals(toWallet.id(), replenishedToWallet.id());
        assertEquals(toWallet.name(), replenishedToWallet.name());
        assertEquals(toWallet.description(), replenishedToWallet.description());
        assertEquals(amount, replenishedToWallet.balance());
    }

    @Test
    void deleteWallet() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_delete");

        //create wallet
        BigDecimal initialBalance = BigDecimal.ZERO;
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );

        assertEquals(initialBalance, wallet.balance());

        String walletId = wallet.id();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/wallet/{walletId}", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        WalletDto deletedWallet = getWallet(token, walletId);

        assertNull(deletedWallet);
    }

    private WalletDto getWallet(String token, String walletId) throws Exception {
        return mapper.readValue(mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), WalletDto.class);
    }
}