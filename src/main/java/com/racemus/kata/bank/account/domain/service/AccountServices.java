package com.racemus.kata.bank.account.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.racemus.kata.bank.account.domain.model.Transaction;

public interface AccountServices {

    /**
     *  Deposit in account
     *
     * @param accountId
     * @param amount
     * @return Transaction
     */
    Transaction deposit(long accountId, BigDecimal amount);

    /**
     *  Withdrawal from account
     *
     * @param accountId
     * @param amount
     * @return Transaction
     */
    Transaction withdrawal(long accountId, BigDecimal amount);

    /**
     * Log history account
     *
     * @param accountId
     * @param startDate
     * @param endDate
     * @return String
     */
    String getAccountHistory(long accountId, LocalDate startDate, LocalDate endDate);
}
