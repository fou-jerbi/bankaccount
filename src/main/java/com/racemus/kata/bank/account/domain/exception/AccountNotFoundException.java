package com.racemus.kata.bank.account.domain.exception;

import lombok.Builder;

public class AccountNotFoundException extends RuntimeException {

    @Builder
    public AccountNotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }
}
