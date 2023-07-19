package com.racemus.kata.bank.account.domain.exception;

import lombok.Builder;

public class NegativeAmountException extends RuntimeException {

    @Builder
    public NegativeAmountException(final String message, Throwable cause) {
        super(message, cause);
    }
}
