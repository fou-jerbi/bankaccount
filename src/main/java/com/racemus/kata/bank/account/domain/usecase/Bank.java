package com.racemus.kata.bank.account.domain.usecase;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import com.racemus.kata.bank.account.domain.exception.NegativeAmountException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.TransactionRepository;

@RequiredArgsConstructor
public class Bank {

    private final Operation operation;
    private final TransactionRepository transactionRepository;

    public Transaction executeOperation(Account account, BigDecimal amount) {
        checkNegativeAmount(amount);
        Transaction transaction = operation.doOperation(account, amount);
        return transactionRepository.save(transaction);
    }

    private void checkNegativeAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw NegativeAmountException
                    .builder().message("Amount" + amount + " can not be negative")
                    .build();
        }
    }

}
