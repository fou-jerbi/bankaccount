package com.racemus.kata.bank.account.domain.repository;

import java.util.Optional;

import com.racemus.kata.bank.account.domain.model.Account;

public interface AccountRepository {

    Optional<Account> getAccountById(long accountId);

    Account saveOrUpdate(Account account);
}
