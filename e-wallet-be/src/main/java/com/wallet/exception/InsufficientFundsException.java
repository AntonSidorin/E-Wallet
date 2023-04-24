package com.wallet.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientFundsException extends RuntimeException {
    private final String walletId;
    private final String name;
    private final BigDecimal balance;
    private final BigDecimal amount;

}
