package com.racemus.kata.bank.account.domain.usecase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.racemus.kata.bank.account.domain.exception.AccountAmountInsufficientException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.OperationType;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.service.TimeService;
import com.racemus.kata.bank.account.domain.service.TransactionIdGenerator;
import com.racemus.kata.bank.account.domain.usecase.WithdrawalOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class WithdrawalOperationTest {

    @Mock
    private TimeService timeService;
    @Mock
    private TransactionIdGenerator transactionIdGenerator;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private WithdrawalOperation withdrawalOperation;


    @Test
    @DisplayName("Should throw NegativeAmountException when amount negative")
     void should_throw_AccountAmountInsufficientException_when_amount_negative() {
        //given
        BigDecimal amount = new BigDecimal(600);
        Account account = Account.builder().balance(new BigDecimal(400)).build();

        //When // Then
        assertThrows(AccountAmountInsufficientException.class, () -> withdrawalOperation.doOperation(account, amount));
        // Then
        verifyNoMoreInteractions(accountRepository);
    }


    @Test
    @DisplayName("Should credit account with amount when withdrawal operation")
     void should_credit_account_with_amount_when_withdrawal_operation() {
        //given
        BigDecimal amount = new BigDecimal(400);
        Account account = Account.builder().balance(new BigDecimal(600)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        withdrawalOperation.doOperation(account, amount);
        // Then
        assertEquals(new BigDecimal(200), account.getBalance());
    }

    @Test
    @DisplayName("Should return withdrawal transaction")
     void should_return_withdrawal_transaction_when_withdrawal_operation() {
        //given
        BigDecimal amount = new BigDecimal(400);
        Account account = Account.builder().balance(new BigDecimal(600)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        Transaction transaction = withdrawalOperation.doOperation(account, amount);
        // Then
        assertEquals(uuid, transaction.getTransactionId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(localDateTime, transaction.getDate());
        Assertions.assertEquals(OperationType.WITHDRAWAL, transaction.getType());
    }

    @Test
    @DisplayName("Should update account in db when withdrawal operation")
     void should_update_account_in_db_when_withdrawal_operation() {
        //given
        BigDecimal amount = new BigDecimal(400);
        Account account = Account.builder().balance(new BigDecimal(600)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        withdrawalOperation.doOperation(account, amount);
        // Then
        verify(accountRepository, times(1)).saveOrUpdate(account);
    }

}
