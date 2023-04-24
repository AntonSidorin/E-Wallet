package com.wallet.service.wallet.transaction;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.wallet.dao.entity.Transaction;
import com.wallet.dao.repository.TransactionRepository;
import com.wallet.dto.TransactionDto;
import com.wallet.service.mapper.transaction.TransactionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    private TransactionMapper mapper = new TransactionMapper();

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getAllTransactionsTest() {

        //given
        String username = "username";
        String walletId = "1";
        LocalDateTime transactionDate = now();
        when(transactionRepository.findByUsernameAndWalletId(username, walletId))
                .thenReturn(createTransactions(walletId, username, transactionDate));

        //when
        List<TransactionDto> transactions = transactionService.getTransactions(username, walletId);

        //then
        assertEquals(convertToDto(createTransactions(walletId, username, transactionDate)), transactions);
    }

    private List<TransactionDto> convertToDto(List<Transaction> transactions) {
        return transactions.stream().map(mapper).toList();
    }

    private List<Transaction> createTransactions(String walletId, String username, LocalDateTime transactionDate) {
        List<Transaction> transactions = new ArrayList<>(3);
        transactions.add(createTransaction("A", BigDecimal.ONE, transactionDate, walletId, username));
        transactions.add(createTransaction("B", BigDecimal.TEN, transactionDate, walletId, username));
        transactions.add(createTransaction("C", BigDecimal.ONE, transactionDate, walletId, username));
        return transactions;
    }

    private Transaction createTransaction(
            String id,
            BigDecimal amount,
            LocalDateTime transactionDate,
            String walletId,
            String username) {
        return new Transaction(
                id,
                null,
                amount,
                BigDecimal.ONE,
                transactionDate,
                walletId,
                username
        );
    }
}