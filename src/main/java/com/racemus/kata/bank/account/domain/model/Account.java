package com.racemus.kata.bank.account.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Account {
    private long accountId;
    private BigDecimal balance;
    private Date creationDate;
}
