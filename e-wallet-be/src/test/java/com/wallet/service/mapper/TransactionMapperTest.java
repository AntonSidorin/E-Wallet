package com.wallet.service.mapper;

import static java.time.LocalDateTime.now;

import com.wallet.dao.entity.Transaction;
import com.wallet.dto.TransactionDto;
import com.wallet.service.mapper.transaction.TransactionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

class TransactionMapperTest {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void transactionMappingTest(){

        //given
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setDescription("Test description");
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTransactionDate(now());
        transaction.setWalletId("walletId");

        //when
        TransactionDto dto = new TransactionMapper().apply(transaction);

        //then
        Assertions.assertEquals(dto.id(), transaction.getId());
        Assertions.assertEquals(dto.description(), transaction.getDescription());
        Assertions.assertEquals(dto.amount(), transaction.getAmount());
        Assertions.assertEquals(dto.transactionDate(), format.format(transaction.getTransactionDate()));

    }

}