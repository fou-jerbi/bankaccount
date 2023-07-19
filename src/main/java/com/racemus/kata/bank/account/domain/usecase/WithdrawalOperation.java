package com.racemus.kata.bank.account.domain.usecase;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

import com.racemus.kata.bank.account.domain.exception.AccountAmountInsufficientException;
import com.racemus.kata.bank.account.domain.exception.NegativeAmountException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.OperationType;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.service.TimeService;
import com.racemus.kata.bank.account.domain.service.TransactionIdGenerator;

@RequiredArgsConstructor
public class WithdrawalOperation implements Operation {

    private final TimeService timeService;
    private final AccountRepository accountRepository;
    private final TransactionIdGenerator transactionIdGenerator;


    @Override
    public Transaction doOperation(Account account, BigDecimal amount) {
        final BigDecimal balance = account.getBalance();
        checkNegativeAmount(amount);
        checkBalanceSufficient(amount, balance);
        Transaction transaction = buildTransaction(account, amount);
        debitAccount(account, amount);
        accountRepository.saveOrUpdate(account);
        return transaction;
    }

    private void debitAccount(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
    }

    private Transaction buildTransaction(Account account, BigDecimal amount) {
        return Transaction.builder()
                .transactionId(transactionIdGenerator.next())
                .accountId(account.getAccountId())
                .amount(amount)
                .type(OperationType.WITHDRAWAL)
                .date(timeService.getCurrentDateTime())
                .build();
    }

    private void checkBalanceSufficient(BigDecimal amount, BigDecimal balance) {
        if (Objects.isNull(balance) || balance.compareTo(amount) < 0) {
            throw AccountAmountInsufficientException.builder().message("you have insufficient amount to retrieve")
                    .build();
        }
    }

    private void checkNegativeAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw NegativeAmountException
                    .builder().message("Amount" + amount + " can not be negative")
                    .build();
        }
    }
}
