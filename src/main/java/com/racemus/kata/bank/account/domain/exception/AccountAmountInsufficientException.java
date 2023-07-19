package com.racemus.kata.bank.account.domain.exception;

import lombok.Builder;

public class AccountAmountInsufficientException extends RuntimeException {

    @Builder
    public AccountAmountInsufficientException(final String message, Throwable cause) {
        super(message, cause);
    }
}
