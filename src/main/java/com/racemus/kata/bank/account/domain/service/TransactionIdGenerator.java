package com.racemus.kata.bank.account.domain.service;

import java.util.UUID;

public class TransactionIdGenerator {

    public UUID next() {
        return UUID.randomUUID();
    }
}
