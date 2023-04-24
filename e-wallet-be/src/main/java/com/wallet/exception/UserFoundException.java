package com.wallet.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserFoundException extends RuntimeException {
    private final String username;
}
