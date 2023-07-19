package com.racemus.kata.bank.account.domain.service.impl;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.racemus.kata.bank.account.domain.exception.AccountNotFoundException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.repository.TransactionRepository;
import com.racemus.kata.bank.account.domain.service.AccountServices;
import com.racemus.kata.bank.account.domain.service.BankFactory;
import com.racemus.kata.bank.account.domain.usecase.Bank;
import com.racemus.kata.bank.account.domain.usecase.DepositOperation;
import com.racemus.kata.bank.account.domain.usecase.WithdrawalOperation;

@AllArgsConstructor
public class AccountServicesImpl implements AccountServices {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final DepositOperation depositOperation;
    private final WithdrawalOperation withdrawalOperation;
    private final BankFactory bankFactory;


    @Override
    public Transaction deposit(long accountId, BigDecimal amount) {
        Account account = getAccountOrFail(accountId);
        Bank bank = bankFactory.build(depositOperation, transactionRepository);
        return bank.executeOperation(account, amount);
    }

    @Override
    public Transaction withdrawal(long accountId, BigDecimal amount) {
        Account account = getAccountOrFail(accountId);
        Bank bank = bankFactory.build(withdrawalOperation, transactionRepository);
        return bank.executeOperation(account, amount);
    }

    @Override
    public String getAccountHistory(long accountId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(accountId, startDate, endDate);
        return logHistoryAccount(transactions);
    }


    private Account getAccountOrFail(long accountId) {
        return accountRepository.getAccountById(accountId)
                .orElseThrow(() -> AccountNotFoundException
                        .builder().message("No Account found for accountId " + accountId)
                        .build());
    }

    private String logHistoryAccount(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s", "TransactionType"));
        sb.append(",");
        sb.append(String.format("%-35s", "Date"));
        sb.append(",");
        sb.append(String.format("%-10s", "Amount") + "\n");

        for (Transaction transaction : transactions) {
            sb.append(String.format("%-12s", transaction.getType().name()));
            sb.append(String.format("%33s", transaction.getDate().toString()));
            sb.append(String.format("%11s", transaction.getAmount()));
            sb.append("\n");
        }
        return sb.toString();
    }

}
