package com.ufrn.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository; // aqui a instância do repositório é injetada pelo spring

    public boolean createAccount(Long number) {
        if (repository.existsByNumber(number)) {
            System.out.println("Número de conta já existe!");
            return false;
        }

        Account account = new Account(number, 0.0);
        repository.save(account);
        return true;
    }

    public double getBalance(Long number) {
        if (repository.existsByNumber(number) == false) {
            System.out.println("Número de conta não existe!");
            return Double.MIN_VALUE;
        }

        Account account = repository.findByNumber(number);
        return account.getBalance();
    }
}
