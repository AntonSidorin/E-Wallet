package com.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException(String message){
        super(message);
    }

}
