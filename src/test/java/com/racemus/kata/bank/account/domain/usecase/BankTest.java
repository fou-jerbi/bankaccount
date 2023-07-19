package com.racemus.kata.bank.account.domain.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.racemus.kata.bank.account.domain.exception.NegativeAmountException;
import com.racemus.kata.bank.account.domain.model.Account;
import com.racemus.kata.bank.account.domain.model.Transaction;
import com.racemus.kata.bank.account.domain.repository.TransactionRepository;
import com.racemus.kata.bank.account.domain.usecase.Bank;
import com.racemus.kata.bank.account.domain.usecase.Operation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BankTest {
    @Mock
    private Operation operation;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private Bank bank;


    @Test
    @DisplayName("Should throw NegativeAmountException when amount negative")
    public void should_throw_NegativeAmountException_when_amount_negative() {
        //given
        final BigDecimal amount = new BigDecimal(-6);
        final Account account = Account.builder().build();
        //When // Then
        assertThrows(NegativeAmountException.class, () -> bank.executeOperation(account, amount));
        // Then
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Should execute operation")
    void should_execute_operation() {
        //given
        final BigDecimal amount = new BigDecimal(100);
        final Account account = Account.builder().balance(new BigDecimal(50)).build();
        final Transaction expected = Transaction.builder().build();
        when(operation.doOperation(account, amount)).thenReturn(expected);
        when(transactionRepository.save(expected)).thenReturn(expected);
        //When // Then
        Transaction actual = bank.executeOperation(account, amount);
        // Then
        assertEquals(expected, actual);
        verify(transactionRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("Should return transaction when operation executed")
    void should_return_transaction_when_operation_executed() {
        //given
        final BigDecimal amount = new BigDecimal(100);
        final Account account = Account.builder().balance(new BigDecimal(50)).build();
        final Transaction expected = Transaction.builder().build();
        when(operation.doOperation(account, amount)).thenReturn(expected);
        when(transactionRepository.save(expected)).thenReturn(expected);
        //When // Then
        Transaction actual = bank.executeOperation(account, amount);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should save transaction when operation executed")
    void should_save_transaction_when_operation_executed() {
        //given
        final BigDecimal amount = new BigDecimal(100);
        final Account account = Account.builder().balance(new BigDecimal(50)).build();
        final Transaction expected = Transaction.builder().build();
        when(operation.doOperation(account, amount)).thenReturn(expected);
        when(transactionRepository.save(expected)).thenReturn(expected);
        //When // Then
        bank.executeOperation(account, amount);
        // Then
        verify(transactionRepository, times(1)).save(expected);
    }

}