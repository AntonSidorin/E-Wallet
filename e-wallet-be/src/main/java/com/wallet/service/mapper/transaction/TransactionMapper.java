package com.wallet.service.mapper.transaction;

import com.wallet.dao.entity.Transaction;
import com.wallet.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Service
public class TransactionMapper implements Function<Transaction, TransactionDto> {

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public TransactionDto apply(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getBalance(),
                formatTransactionDate(transaction.getTransactionDate()),
                transaction.getWalletId()
        );
    }

    private String formatTransactionDate(LocalDateTime date){
        return format.format(date);
    }

}
