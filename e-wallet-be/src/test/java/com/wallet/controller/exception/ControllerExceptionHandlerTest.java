package com.wallet.controller.exception;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.controller.AbstractControllerTest;
import com.wallet.controller.auth.RegisterRequest;
import com.wallet.dto.WalletDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerExceptionHandlerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deleteNonExistingWalletTest() throws Exception {

        //register user
        String username = "user_no_delete";
        String token = getTokenViaRegistry(username);

        String walletId = "walletId";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/wallets/{walletId}", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString(walletId)));
    }

    @Test
    void topupWithEmptyWalletId() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_topup_empty_id");

        //create wallet
        BigDecimal initialBalance = BigDecimal.ZERO;
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );
        assertEquals(initialBalance, wallet.balance());

        //topup wallet
        String walletId = StringUtils.EMPTY;
        BigDecimal amount = BigDecimal.TEN;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/wallets/{walletId}/topup/{amount}", walletId, amount)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteWalletWithFundsTest() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_delete_with_funds");

        //create wallet
        BigDecimal initialBalance = BigDecimal.TEN;
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );

        assertEquals(initialBalance, wallet.balance());

        String walletId = wallet.id();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/wallets/{walletId}", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString(walletId)));
    }

    @Test
    void registerAlreadyExistingUserTest() throws Exception {

        //register user
        String username = "register_user";
        getTokenViaRegistry(username);

        RegisterRequest request = new RegisterRequest(null, null, username, "password2");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString(username)));
    }

    @Test
    void getBalanceForNonExistingWalletTest() throws Exception {

        //register user
        String username = "user_balance";
        String token = getTokenViaRegistry(username);

        String walletId = "walletId";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{walletId}/balance", walletId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString(walletId)));
    }

    @Test
    void withdrawInsufficientFundsTest() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_withdraw_no_funds");

        //create wallet
        BigDecimal initialBalance = BigDecimal.ONE.setScale(2, RoundingMode.UP);
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );

        assertEquals(initialBalance, wallet.balance());

        //withdraw wallet
        String walletId = wallet.id();
        BigDecimal amount = BigDecimal.TEN;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/wallets/{walletId}/withdraw/{amount}", walletId, amount)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString(initialBalance.toString())));

    }

    @Test
    void createWalletWithoutName() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_wallet_without_name");

        //create wallet
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallets")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(mapper.writeValueAsBytes(
                                new WalletDto(null, null, "Wallet description", BigDecimal.ONE))
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", equalTo("Name shouldn't be blank")));

    }

    @Test
    void withdrawMoreThan10000Test() throws Exception {

        //register user
        String token = getTokenViaRegistry("user_withdraw_bad_request");

        //create wallet
        BigDecimal initialBalance = BigDecimal.ONE.setScale(2, RoundingMode.UP);
        WalletDto wallet = createWallet(
                token,
                new WalletDto(null, "Wallet name", "Wallet description", initialBalance)
        );

        assertEquals(initialBalance, wallet.balance());

        //withdraw wallet
        String walletId = wallet.id();
        BigDecimal amount = BigDecimal.valueOf(20000);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/wallets/{walletId}/withdraw/{amount}", walletId, amount)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0]", containsString("Withdraw amount must be less than 10000.")));

    }
}