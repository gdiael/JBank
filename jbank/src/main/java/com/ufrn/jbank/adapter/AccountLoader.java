package com.ufrn.jbank.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.repository.AccountRepository;

@Component
public class AccountLoader {

    @Autowired
    private AccountRepository repository;

    public void loadDummyAccounts() {
        repository.save(new Account(12345L, 1000.0));
        repository.save(new Account(67890L, 2000.0));
        repository.save(new Account(11223L, 3500.0));
        repository.save(new Account(44556L, 150.75));
        repository.save(new Account(77889L, 9800.0));
        repository.save(new Account(99001L, 50.0));
        repository.save(new Account(22334L, 720.30));
        repository.save(new Account(55667L, 4300.0));
        repository.save(new Account(88990L, 12000.0));
        repository.save(new Account(33445L, 275.90));
        repository.save(new Account(10101L, 640.0));
        repository.save(new Account(20202L, 8450.5));
        repository.save(new Account(30303L, 120.0));
        repository.save(new Account(40404L, 9999.99));
        repository.save(new Account(50505L, 75.25));
        repository.save(new Account(60606L, 1800.0));
        repository.save(new Account(70707L, 250.0));
        repository.save(new Account(80808L, 6700.0));
        repository.save(new Account(90909L, 430.40));
        repository.save(new Account(11111L, 300.0));
        repository.save(new Account(22222L, 15000.0));
        repository.save(new Account(33333L, 980.0));
        repository.save(new Account(44444L, 210.10));
        repository.save(new Account(55555L, 7650.75));
        repository.save(new Account(66666L, 50.5));
        repository.save(new Account(77777L, 870.0));
        repository.save(new Account(88888L, 9200.0));
        repository.save(new Account(99999L, 110.0));
        repository.save(new Account(12121L, 4300.0));
        repository.save(new Account(13131L, 250.25));
    }

}

