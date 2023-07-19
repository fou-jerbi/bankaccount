package com.racemus.kata.bank.account.domain.usecase;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.OperationType;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.service.TimeService;
import com.racemus.kata.bank.account.domain.service.TransactionIdGenerator;

@RequiredArgsConstructor
public class DepositOperation implements Operation {

    private final TimeService timeService;
    private final AccountRepository accountRepository;
    private final TransactionIdGenerator transactionIdGenerator;


    @Override
    public Transaction doOperation(Account account, BigDecimal amount) {
        Transaction transaction = buildDepositTransaction(account.getAccountId(), amount);
        creditAccount(account, amount);
        accountRepository.saveOrUpdate(account);
       return transaction;
    }


    private void creditAccount(Account account, BigDecimal amount) {
        BigDecimal newBalance = Optional.ofNullable(account.getBalance())
                .map(balance -> balance.add(amount))
                .orElse(amount);
        account.setBalance(newBalance);
    }

    private Transaction buildDepositTransaction(long accountId, BigDecimal amount) {
        return Transaction.builder()
                .transactionId(transactionIdGenerator.next())
                .accountId(accountId)
                .amount(amount)
                .type(OperationType.DEPOSIT)
                .date(timeService.getCurrentDateTime())
                .build();
    }

}
