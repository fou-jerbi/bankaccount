package com.racemus.kata.bank.account.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    private UUID transactionId;
    private long accountId;
    private LocalDateTime date;
    private BigDecimal amount;
    private OperationType type;
}
