package com.wallet.service.wallet;

import com.wallet.dto.TransactionDto;
import com.wallet.dto.WalletDto;
import com.wallet.exception.NegativeAmountException;
import com.wallet.security.JwtService;
import com.wallet.service.wallet.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletFacade {

    public static final String AMOUNT_IS_NEGATIVE_MESSAGE = "The amount should be not negative";

    private final JwtService jwtService;
    private final WalletService walletService;
    private final WalletBalanceService balanceService;
    private final TransactionService transactionService;

    public WalletDto create(String token, WalletDto walletDto) {
        return walletService.create(jwtService.extractUsername(token), walletDto);
    }

    public List<WalletDto> getWallets(String token) {
        return walletService.getWalletsByUsername(jwtService.extractUsername(token));
    }

    public Optional<WalletDto> wallet(String token, String walletId) {
        return walletService.getWalletById(jwtService.extractUsername(token), walletId);
    }

    @Transactional(readOnly = true)
    public BigDecimal balance(String token, String walletId) {
        return balanceService.balance(jwtService.extractUsername(token), walletId);
    }

    public Optional<WalletDto> topup(String token, String walletId, BigDecimal amount) {

        String username = jwtService.extractUsername(token);

        if (amount == null || amount.signum() == 0){
            return walletService.getWalletById(username, walletId);
        } else if (amount.signum() < 0) {
            throw new NegativeAmountException(AMOUNT_IS_NEGATIVE_MESSAGE);
        }

        return balanceService.topup(username, walletId, amount);
    }

    public Optional<WalletDto> withdraw(String token, String walletId, BigDecimal amount) {

        String username = jwtService.extractUsername(token);

        if (amount == null || amount.signum() == 0){
            return walletService.getWalletById(username, walletId);
        } else if (amount.signum() < 0) {
            throw new NegativeAmountException(AMOUNT_IS_NEGATIVE_MESSAGE);
        }

        return balanceService.withdraw(username, walletId, amount);
    }

    public Optional<WalletDto> transfer(String token, String fromWalletId, String toWalletId, BigDecimal amount) {
        String username = jwtService.extractUsername(token);

        if (amount == null || amount.signum() == 0){
            return walletService.getWalletById(username, fromWalletId);
        } else if (amount.signum() < 0) {
            throw new NegativeAmountException(AMOUNT_IS_NEGATIVE_MESSAGE);
        }

        return balanceService.transfer(username, fromWalletId, toWalletId, amount);
    }

    public void delete(String token, String walletId) {
        walletService.delete(jwtService.extractUsername(token), walletId);
    }
    public List<TransactionDto> getTransactions(String token, String walletId) {
        return transactionService.getTransactions(jwtService.extractUsername(token), walletId);
    }
}
