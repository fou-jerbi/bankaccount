package com.racemus.kata.bank.account.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.racemus.kata.bank.account.domain.model.Transaction;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findTransactionById(long transactionId);

    List<Transaction> findByDateBetween(long accountId, LocalDate start, LocalDate end);
}
