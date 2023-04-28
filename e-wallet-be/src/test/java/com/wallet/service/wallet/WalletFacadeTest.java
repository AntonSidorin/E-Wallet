package com.wallet.service.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.dto.WalletDto;
import com.wallet.exception.NegativeAmountException;
import com.wallet.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WalletFacadeTest {

    @Mock
    private WalletBalanceService walletBalanceService;

    @Mock
    private JwtService jwtService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletFacade walletFacade;

    @Test
    void getWalletTest() {

        //given
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);

        String walletId = "1";
        Optional<WalletDto> walletDto = Optional.of(createWallet(walletId, BigDecimal.TEN));
        when(walletService.getWalletById(username, walletId)).thenReturn(walletDto);

        //when
        Optional<WalletDto> actualWalletDto = walletFacade.wallet(token, walletId);

        //then
        assertEquals(walletDto, actualWalletDto);
    }

    @Test
    void createWalletTest() {

        //given
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);

        String walletId = "1";
        WalletDto walletDto = createWallet(walletId, BigDecimal.TEN);
        when(walletService.create(username, walletDto)).thenReturn(walletDto);


        //when
        WalletDto actualWalletDto = walletFacade.create(token, walletDto);

        //then
        assertEquals(walletDto, actualWalletDto);
    }

    @Test
    void deleteWalletTest() {

        //given
        String walletId = "1";
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);

        //when
        walletFacade.delete(token, walletId);

        //then
        verify(walletService, atMostOnce()).delete(username, walletId);
    }

    @Test
    void balanceTest() {

        //given
        String walletId = "1";
        String token = "token";
        String username = "username";
        BigDecimal amount = BigDecimal.TEN;
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(walletBalanceService.balance(username, walletId)).thenReturn(amount);

        //when
        BigDecimal balance = walletFacade.balance(token, walletId);

        //then
        assertEquals(amount, balance);
    }

    @Test
    void topupWalletTest() {

        //given
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);

        String walletId = "walletId";
        BigDecimal amount = BigDecimal.TEN;
        Optional<WalletDto> wallet = Optional.of(createWallet(walletId, BigDecimal.TEN));
        when(walletBalanceService.topup(username, walletId, amount)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.topup(token, walletId, amount);

        //then
        assertEquals(wallet, actualWallet);

    }

    @Test
    void topupNegativeAmountTest() {
        BigDecimal amount = BigDecimal.TEN.negate();
        NegativeAmountException exception = assertThrows(
                NegativeAmountException.class,
                () -> walletFacade.topup("", "1", amount)
        );

        Assertions.assertTrue(WalletFacade.AMOUNT_IS_NEGATIVE_MESSAGE.contains(exception.getMessage()));
    }


    @Test
    void topupZeroAmountTest() {

        //given
        String walletId = "1";
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);
        Optional<WalletDto> wallet = Optional.of(createWallet(walletId, BigDecimal.TEN));
        when(walletService.getWalletById(username, walletId)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.topup(token, walletId, BigDecimal.ZERO);

        //then
        walletBalanceServiceIsNotInvoked();
        assertEquals(wallet, actualWallet);
    }

    @Test
    void withdrawFromWalletTest() {

        //given
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);

        String walletId = "walletId";
        BigDecimal amount = BigDecimal.TEN;
        Optional<WalletDto> wallet = Optional.of(createWallet(walletId, BigDecimal.TEN));
        when(walletBalanceService.withdraw(username, walletId, amount)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.withdraw(token, walletId, amount);

        //then
        assertEquals(wallet, actualWallet);

    }

    @Test
    void withdrawNegativeAmountTest() {

        BigDecimal amount = BigDecimal.TEN.negate();
        NegativeAmountException exception = assertThrows(
                NegativeAmountException.class,
                () -> walletFacade.withdraw("", "1", amount)
        );

        Assertions.assertTrue(WalletFacade.AMOUNT_IS_NEGATIVE_MESSAGE.contains(exception.getMessage()));

    }

    @Test
    void withdrawZeroAmountTest() {

        //given
        String walletId = "1";
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);
        Optional<WalletDto> wallet = Optional.of(createWallet(walletId, BigDecimal.TEN));
        when(walletService.getWalletById(username, walletId)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.withdraw(token, walletId, BigDecimal.ZERO);

        //then
        walletBalanceServiceIsNotInvoked();
        assertEquals(wallet, actualWallet);
    }

    @Test
    void transferAmountTest() {

        //given
        String fromWalletId = "1";
        String toWalletId = "2";
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);
        Optional<WalletDto> wallet = Optional.of(createWallet(fromWalletId, BigDecimal.ZERO));
        when(walletBalanceService.transfer(username, fromWalletId, toWalletId, BigDecimal.TEN)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.transfer(token, fromWalletId, toWalletId, BigDecimal.TEN);

        //then
        assertEquals(wallet, actualWallet);
    }

    @Test
    void transferNegativeAmountTest() {
        BigDecimal amount = BigDecimal.TEN.negate();
        NegativeAmountException exception = assertThrows(
                NegativeAmountException.class,
                () -> walletFacade.transfer("", "1", "2", amount)
        );

        Assertions.assertTrue(WalletFacade.AMOUNT_IS_NEGATIVE_MESSAGE.contains(exception.getMessage()));
    }

    @Test
    void transferZeroAmountTest() {

        //given
        String fromWalletId = "1";
        String toWalletId = "2";
        String token = "token";
        String username = "username";
        when(jwtService.extractUsername(token)).thenReturn(username);
        Optional<WalletDto> wallet = Optional.of(createWallet(fromWalletId, BigDecimal.TEN));
        when(walletService.getWalletById(username, fromWalletId)).thenReturn(wallet);

        //when
        Optional<WalletDto> actualWallet = walletFacade.transfer(token, fromWalletId, toWalletId, BigDecimal.ZERO);

        //then
        walletBalanceServiceIsNotInvoked();
        assertEquals(wallet, actualWallet);
    }

    private static WalletDto createWallet(String walletId, BigDecimal amount) {
        return new WalletDto(walletId, null, null, amount);
    }

    private void walletBalanceServiceIsNotInvoked() {
        verify(walletBalanceService, never()).topup(anyString(), anyString(), any(BigDecimal.class));
        verify(walletBalanceService, never()).withdraw(anyString(), anyString(), any(BigDecimal.class));
        verify(walletBalanceService, never()).transfer(anyString(), anyString(), anyString(), any(BigDecimal.class));
    }
}