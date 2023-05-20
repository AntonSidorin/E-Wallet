package com.wallet.controller.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResult {

    private final List<String> messages = new ArrayList<>();

    public static ErrorResult builder() {
        return new ErrorResult();
    }

    public ErrorResult addMessage(String message) {
        messages.add(message);
        return this;
    }

}
