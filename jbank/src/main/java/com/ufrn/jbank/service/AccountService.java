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

    public boolean deposit(Long number, double amount) {
        if (repository.existsByNumber(number) == false) {
            System.out.println("Número de conta não existe!");
            return false;
        }

        Account account = repository.findByNumber(number);
        account.setBalance(account.getBalance() + amount);
        repository.save(account);
        return true;
    }

    public boolean withdraw(Long number, double amount) {
        if (repository.existsByNumber(number) == false) {
            System.out.println("Número de conta não existe!");
            return false;
        }

        Account account = repository.findByNumber(number);
        account.setBalance(account.getBalance() - amount);
        repository.save(account);
        return true;
    }

    public boolean transfer(Long fromNumber, Long toNumber, double amount) {
        if (repository.existsByNumber(fromNumber) == false) {
            System.out.println("Número de conta 1 não existe!");
            return false;
        }
        if (repository.existsByNumber(toNumber) == false) {
            System.out.println("Número de conta 2 não existe!");
            return false;
        }

        Account fromAccount = repository.findByNumber(fromNumber);
        Account toAccount = repository.findByNumber(toNumber);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        repository.save(fromAccount);
        repository.save(toAccount);
        return true;
    }
}
