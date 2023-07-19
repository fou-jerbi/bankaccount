package com.racemus.kata.bank.account.domain.usecase;

import java.math.BigDecimal;

import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.Transaction;

public interface Operation {

    Transaction doOperation(Account account, BigDecimal amount);
}
