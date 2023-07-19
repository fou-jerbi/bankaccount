package com.racemus.kata.bank.account.domain.service;

import com.racemus.kata.bank.account.domain.repository.TransactionRepository;
import com.racemus.kata.bank.account.domain.usecase.Bank;
import com.racemus.kata.bank.account.domain.usecase.DepositOperation;
import com.racemus.kata.bank.account.domain.usecase.WithdrawalOperation;

public class BankFactory {

    public Bank build(DepositOperation depositOperation, TransactionRepository transactionRepository) {
        return new Bank(depositOperation, transactionRepository);
    }

    public Bank build(WithdrawalOperation withdrawalOperation, TransactionRepository transactionRepository) {
        return new Bank(withdrawalOperation, transactionRepository);
    }


}
