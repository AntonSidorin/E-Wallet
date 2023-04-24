package com.wallet.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WalletNotFoundException extends RuntimeException {
    private final String username;
    private final String walletId;
}
