package com.racemus.kata.bank.account.domain.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.OperationType;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.AccountRepository;
import com.racemus.kata.bank.account.domain.service.TimeService;
import com.racemus.kata.bank.account.domain.service.TransactionIdGenerator;
import com.racemus.kata.bank.account.domain.usecase.DepositOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class DepositOperationTest {

    @Mock
    private TimeService timeService;
    @Mock
    private TransactionIdGenerator transactionIdGenerator;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private DepositOperation depositOperation;


    @Test
    @DisplayName("Should credit account when deposit operation")
     void should_credit_account_with_amount_when_deposit_operation() {
        //given
        BigDecimal amount = new BigDecimal(600);
        Account account = Account.builder().balance(new BigDecimal(400)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        depositOperation.doOperation(account, amount);
        // Then
        assertEquals(new BigDecimal(1000), account.getBalance());
    }

    @Test
    @DisplayName("Should return deposit transaction when deposit operation")
     void should_return_deposit_transaction_when_deposit_operation() {
        //given
        BigDecimal amount = new BigDecimal(600);
        Account account = Account.builder().balance(new BigDecimal(400)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        Transaction transaction = depositOperation.doOperation(account, amount);
        // Then
        assertEquals(uuid, transaction.getTransactionId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(localDateTime, transaction.getDate());
        Assertions.assertEquals(OperationType.DEPOSIT, transaction.getType());
    }

    @Test
    @DisplayName("Should update account in db when deposit operation")
     void should_update_account_in_db_when_deposit_operation() {
        //given
        BigDecimal amount = new BigDecimal(600);
        Account account = Account.builder().balance(new BigDecimal(400)).build();

        LocalDateTime localDateTime = LocalDateTime.now();
        when(timeService.getCurrentDateTime()).thenReturn(localDateTime);

        UUID uuid = UUID.randomUUID();
        when(transactionIdGenerator.next()).thenReturn(uuid);
        // When
        depositOperation.doOperation(account, amount);
        // Then
        verify(accountRepository, times(1)).saveOrUpdate(account);
    }

}
