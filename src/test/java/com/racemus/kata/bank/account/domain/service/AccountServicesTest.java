package com.racemus.kata.bank.account.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.racemus.kata.bank.account.domain.exception.AccountNotFoundException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.OperationType;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.repository.TransactionRepository;
import com.racemus.kata.bank.account.domain.service.BankFactory;
import com.racemus.kata.bank.account.domain.service.impl.AccountServicesImpl;
import com.racemus.kata.bank.account.domain.usecase.Bank;
import com.racemus.kata.bank.account.domain.usecase.DepositOperation;
import com.racemus.kata.bank.account.domain.usecase.WithdrawalOperation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServicesTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BankFactory bankFactory;
    @Mock
    private DepositOperation depositOperation;
    @Mock
    private WithdrawalOperation withdrawalOperation;

    @InjectMocks
    private AccountServicesImpl accountServiceImpl;


    @Test
    @DisplayName("Should deposit amount on account nominal case")
    void should_deposit_amount_on_account_nominal_case() {
        // Given
        long accountId = 314L;
        BigDecimal amount = new BigDecimal(300);

        Account account = Account.builder().build();
        Transaction expected = Transaction.builder().build();
        Bank bank = new Bank(depositOperation, transactionRepository);

        when(accountRepository.getAccountById(accountId)).thenReturn(Optional.of(account));
        when(bankFactory.build(depositOperation, transactionRepository)).thenReturn(bank);
        when(bank.executeOperation(account, amount)).thenReturn(expected);
        // When
        Transaction actual = accountServiceImpl.deposit(accountId, amount);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when account not found during deposit")
    void should_throw_AccountNotFoundException_when_account_not_found_during_deposit() {
        // Given
        long accountId = 314L;
        BigDecimal amount = new BigDecimal(300);

        when(accountRepository.getAccountById(accountId)).thenReturn(Optional.empty());
        // When // Then
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.deposit(accountId, amount));
    }

    @Test
    @DisplayName("Should withdrawal amount from account nominal case")
    void should_withdrawal_amount_from_account_nominal_case() {
        // Given
        long accountId = 314L;
        BigDecimal amount = new BigDecimal(300);

        Account account = Account.builder().build();
        Transaction expected = Transaction.builder().build();
        Bank bank = new Bank(withdrawalOperation, transactionRepository);

        when(accountRepository.getAccountById(accountId)).thenReturn(Optional.of(account));
        when(bankFactory.build(withdrawalOperation, transactionRepository)).thenReturn(bank);
        when(bank.executeOperation(account, amount)).thenReturn(expected);
        // When
        Transaction actual = accountServiceImpl.withdrawal(accountId, amount);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when account not found during withdrawal")
    void should_throw_AccountNotFoundException_when_account_not_found_during_withdrawal() {
        // Given
        long accountId = 314L;
        BigDecimal amount = new BigDecimal(300);

        when(accountRepository.getAccountById(accountId)).thenReturn(Optional.empty());
        // When // Then
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.withdrawal(accountId, amount));
    }


    @Test
    @DisplayName("Should log account history when list transaction not empty")
    void should_log_account_history_when_list_transaction_not_empty() {
        // Given
        long accountId = 314L;
        LocalDate endDate = LocalDate.of(2023, 07, 19);
        LocalDate startDate = LocalDate.of(2023, 07, 17);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .date(LocalDateTime.of(2022, 5, 22, 0, 0))
                        .type(OperationType.DEPOSIT)
                        .amount(new BigDecimal(100))
                        .build(),
                Transaction.builder()
                        .date(LocalDateTime.of(2022, 5, 30, 0, 0))
                        .type(OperationType.WITHDRAWAL)
                        .amount(new BigDecimal(50))
                        .build()
        );

        when(transactionRepository.findByDateBetween(accountId, startDate, endDate)).thenReturn(transactions);

        String expected = "TransactionType,Date                               ,Amount    \n" +
                "DEPOSIT                      2022-05-22T00:00        100\n" +
                "WITHDRAWAL                   2022-05-30T00:00         50\n";
        // When
        String actual = accountServiceImpl.getAccountHistory(accountId, startDate, endDate);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should log empty history when list transaction is empty")
    void should_log_empty_history_when_list_transaction_is_empty() {
        // Given
        long accountId = 314L;
        LocalDate endDate = LocalDate.of(2023, 07, 19);
        LocalDate startDate = LocalDate.of(2023, 07, 17);

        when(transactionRepository.findByDateBetween(accountId, startDate, endDate)).thenReturn(List.of());

        String expected = "TransactionType,Date                               ,Amount    \n";

        // When
        String actual = accountServiceImpl.getAccountHistory(accountId, startDate, endDate);
        // Then
        assertEquals(expected, actual);
    }

}
