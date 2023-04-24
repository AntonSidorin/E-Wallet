package com.wallet.service.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.TransactionRepository;
import com.wallet.dao.repository.WalletRepository;
import com.wallet.dto.WalletDto;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.service.mapper.wallet.WalletToWalletDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WalletBalanceServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    private WalletToWalletDtoMapper walletToWalletDtoMapper = new WalletToWalletDtoMapper();

    @InjectMocks
    private WalletBalanceService walletBalanceService;

    @Test
    void balanceForWalletWithNullIdTest() {
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () ->
                walletBalanceService.balance("username", null)
        );
        assertNull(exception.getWalletId());
        assertEquals("username", exception.getUsername());
    }

    @Test
    void walletBalanceTest() {

        //given
        String walletId = "walletId";
        String username = "username";
        BigDecimal balance = BigDecimal.TEN;

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(balance);
        wallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));

        //when
        BigDecimal actualBalance = walletBalanceService.balance(username, walletId);

        //then
        assertEquals(balance, actualBalance);
    }

    @Test
    void walletWithdrawTest() {

        //given
        String walletId = "walletId";
        String username = "username";
        BigDecimal balance = BigDecimal.TEN;
        BigDecimal amount = BigDecimal.ONE;

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(balance);
        wallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        Optional<WalletDto> walletDto = walletBalanceService.withdraw(username, walletId, amount);

        //then
        assertTrue(walletDto.isPresent());
        assertEquals(walletId, walletDto.get().id());
        assertEquals(balance.subtract(amount), walletDto.get().balance());

    }

    @Test
    void walletWithdrawInsufficientFundsTest() {

        //given
        String walletId = "walletId";
        String walletName = "walletName";
        String username = "username";
        BigDecimal balance = BigDecimal.ONE;
        BigDecimal amount = BigDecimal.TEN;

        Wallet wallet = new Wallet();
        wallet.setName(walletName);
        wallet.setId(walletId);
        wallet.setBalance(balance);
        wallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));

        //when
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () ->
                walletBalanceService.withdraw(username, walletId, amount)
        );

        //then
        assertEquals(walletId, exception.getWalletId());
        assertEquals(walletName, exception.getName());
        assertEquals(balance, exception.getBalance());
        assertEquals(amount, exception.getAmount());
    }

    @Test
    void walletTopupTest() {

        //given
        String walletId = "walletId";
        String username = "username";
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ONE;

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(balance);
        wallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        Optional<WalletDto> walletDto = walletBalanceService.topup(username, walletId, amount);

        //then
        assertTrue(walletDto.isPresent());
        assertEquals(walletId, walletDto.get().id());
        assertEquals(amount, walletDto.get().balance());
    }

    @Test
    void transferAmountTest() {

        //given
        String username = "username";
        String fromWalletId = "1";

        Wallet fromWallet = new Wallet();
        fromWallet.setId(fromWalletId);
        fromWallet.setBalance(BigDecimal.TEN);
        fromWallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, fromWalletId)).thenReturn(Optional.of(fromWallet));

        String toWalletId = "2";
        Wallet toWallet = new Wallet();
        toWallet.setId(toWalletId);
        toWallet.setBalance(BigDecimal.ZERO);
        when(walletRepository.findById(toWalletId)).thenReturn(Optional.of(toWallet));

        BigDecimal amount = BigDecimal.TEN;
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        Optional<WalletDto> actualWallet = walletBalanceService.transfer(username, fromWalletId, toWalletId, amount);

        //then
        assertEquals(Optional.of(walletToWalletDtoMapper.apply(fromWallet)), actualWallet);

    }

    @Test
    void transferAmountFromNonExistentAccountTest() {

        //given
        String username = "username";
        String fromWalletId = "nonExistentAccount";

        Wallet fromWallet = new Wallet();
        fromWallet.setId(fromWalletId);
        fromWallet.setBalance(BigDecimal.TEN);
        fromWallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, fromWalletId)).thenReturn(Optional.empty());

        String toWalletId = "2";
        BigDecimal amount = BigDecimal.TEN;

        //when
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () ->
                walletBalanceService.transfer(username, fromWalletId, toWalletId, amount)
        );

        //then
        assertEquals(username, exception.getUsername());
        assertEquals(fromWalletId, exception.getWalletId());

    }

    @Test
    void transferAmountToNonExistentAccountTest() {

        //given
        String username = "username";
        String fromWalletId = "1";

        Wallet fromWallet = new Wallet();
        fromWallet.setId(fromWalletId);
        fromWallet.setBalance(BigDecimal.TEN);
        fromWallet.setUsername(username);
        when(walletRepository.findByUsernameAndId(username, fromWalletId)).thenReturn(Optional.of(fromWallet));

        String toWalletId = "nonExistentAccount";
        Wallet toWallet = new Wallet();
        toWallet.setId(toWalletId);
        toWallet.setBalance(BigDecimal.ZERO);
        when(walletRepository.findById(toWalletId)).thenReturn(Optional.empty());

        BigDecimal amount = BigDecimal.TEN;

        //when
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () ->
                walletBalanceService.transfer(username, fromWalletId, toWalletId, amount)
        );

        //then
        assertNull(exception.getUsername());
        assertEquals(toWalletId, exception.getWalletId());

    }
}