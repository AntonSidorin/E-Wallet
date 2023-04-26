package com.wallet.service.wallet;

import static java.time.LocalDateTime.now;

import com.wallet.dao.entity.Transaction;
import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.TransactionRepository;
import com.wallet.dao.repository.WalletRepository;
import com.wallet.dto.WalletDto;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.service.mapper.wallet.WalletToWalletDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WalletBalanceService {

    private final WalletRepository walletRepository;
    private final WalletToWalletDtoMapper walletToWalletDtoMapper;

    private final TransactionRepository transactionRepository;

    public BigDecimal balance(String username, String walletId) {
        return Optional.ofNullable(getWalletById(username, walletId))
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException(username, walletId));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<WalletDto> topup(String username, String walletId, BigDecimal amount) {

        Wallet wallet = getWalletById(username, walletId);

        createToTransaction("topup", amount, wallet);

        return topupWallet(wallet, amount);

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<WalletDto> withdraw(String username, String walletId, BigDecimal amount) {

        Wallet wallet = getWalletById(username, walletId);

        validateBalance(wallet, amount);

        createFromTransaction("withdraw", amount, wallet);

        return withdrawFromWallet(wallet, amount);

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<WalletDto> transfer(String fromUsername, String fromWalletId, String toWalletId, BigDecimal amount) {

        Wallet fromWallet = getWalletById(fromUsername, fromWalletId);

        validateBalance(fromWallet, amount);

        Wallet toWallet = getWalletById(toWalletId);

        createFromTransaction("transfer" ,amount, fromWallet);
        createToTransaction("transfer" ,amount, toWallet);

        Optional<WalletDto> withdrawResult = withdrawFromWallet(fromWallet, amount);

        topupWallet(toWallet, amount);

        return withdrawResult;

    }

    private Optional<WalletDto> topupWallet(Wallet toWallet, BigDecimal amount) {
        return Optional.ofNullable(toWallet).map(wallet -> {
                    wallet.setBalance(wallet.getBalance().add(amount));
                    return walletRepository.save(wallet);
                })
                .map(walletToWalletDtoMapper);
    }

    private Optional<WalletDto> withdrawFromWallet(Wallet fromWallet, BigDecimal amount) {
        return Optional.ofNullable(fromWallet).map(wallet -> {
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                    return walletRepository.save(wallet);
                })
                .map(walletToWalletDtoMapper);
    }

    private void createFromTransaction(String description, BigDecimal amount, Wallet wallet) {
        Transaction transaction = createTransactionTemplate(description, amount, wallet);
        transaction.setBalance(wallet.getBalance().subtract(amount));
        transactionRepository.save(transaction);
    }

    private void createToTransaction(String description, BigDecimal amount, Wallet wallet) {
        Transaction transaction = createTransactionTemplate(description, amount, wallet);
        transaction.setBalance(wallet.getBalance().add(amount));
        transactionRepository.save(transaction);
    }

    private Transaction createTransactionTemplate(String description, BigDecimal amount, Wallet wallet){
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setTransactionDate(now());
        transaction.setWalletId(wallet.getId());
        transaction.setUsername(wallet.getUsername());
        return transaction;
    }

    private void validateBalance(Wallet wallet, BigDecimal amount) {
        if(wallet.getBalance().compareTo(amount) < 0){
            throw new InsufficientFundsException(wallet.getId(), wallet.getName(), wallet.getBalance(), amount);
        }
    }

    private Wallet getWalletById(String username, String walletId) {
        return walletRepository
                .findByUsernameAndId(username, walletId)
                .orElseThrow(() -> new WalletNotFoundException(username, walletId));
    }

    private Wallet getWalletById(String walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException(null, walletId));
    }
}
