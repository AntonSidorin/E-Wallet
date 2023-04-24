package com.wallet.dto;

import java.math.BigDecimal;

public record TransactionDto(
        String id,
        String description,
        BigDecimal amount,
        BigDecimal balance,
        String transactionDate,
        String walletId) {
}
