package com.wallet.controller.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.wallet.exception.InsufficientFundsException;
import com.wallet.exception.NegativeAmountException;
import com.wallet.exception.UserFoundException;
import com.wallet.exception.WalletBalanceIsNotZero;
import com.wallet.exception.WalletNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(WalletBalanceIsNotZero.class)
    ErrorResult handleWalletBalanceIsNotZeroException(WalletBalanceIsNotZero e) {
        return ErrorResult.builder().addMessage("Move funds in order to delete wallet " + e.getWalletId());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(UserFoundException.class)
    ErrorResult handleUserFoundExceptionException(UserFoundException e) {
        return ErrorResult.builder().addMessage("User " + e.getUsername() + " has been already registered.");
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(WalletNotFoundException.class)
    ErrorResult handleWalletNotFoundExceptionException(WalletNotFoundException e) {
        String username = e.getUsername();
        String message = "Wallet " + e.getWalletId() + " has not been found.";
        if (StringUtils.isNoneBlank(username)) {
            message += " for user " + username;
        }
        return ErrorResult.builder().addMessage(message);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(NegativeAmountException.class)
    ErrorResult handleNegativeAmountExceptionException(NegativeAmountException e) {
        return ErrorResult.builder().addMessage(e.getMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(InsufficientFundsException.class)
    ErrorResult handleInsufficientFundsExceptionException(InsufficientFundsException e) {
        return ErrorResult.builder().addMessage("Wallet has insufficient funds " + e.getBalance());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResult handleValidationExceptionException(MethodArgumentNotValidException e) {
        ErrorResult builder = ErrorResult.builder();
        e.getBindingResult().getAllErrors().forEach(error -> builder.addMessage(error.getDefaultMessage()));
        return builder;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResult handleConstraintViolationException(ConstraintViolationException e) {
        return ErrorResult.builder().addMessage(e.getMessage());
    }

}
