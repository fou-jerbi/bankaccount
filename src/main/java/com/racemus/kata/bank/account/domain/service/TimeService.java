package com.racemus.kata.bank.account.domain.service;

import java.time.LocalDateTime;

public class TimeService {

    public LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now();
    }
}
