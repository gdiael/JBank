package com.ufrn.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.model.BonusAccount;
import com.ufrn.jbank.model.SavingsAccount;
import com.ufrn.jbank.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository; // aqui a instância do repositório é injetada pelo spring

    @Autowired
    private BonusCalculatorService bonusCalculatorService;

    public boolean createAccount(Long number) {
        if (repository.existsByNumber(number)) {
            System.out.println("Número de conta já existe!");
            return false;
        }

        Account account = new Account(number, 0.0);
        repository.save(account);
        return true;
    }

    public boolean createBonusAccount(Long number) {
        if (repository.existsByNumber(number)) {
            System.out.println("Número de conta já existe!");
            return false;
        }

        Account account = new BonusAccount(number, 0, 10); // contas de bonus novas devem ser criadas com 10 pontos de bonus
        repository.save(account);
        return true;
    }

    public boolean createSavingsAccount(Long number) {
        if (repository.existsByNumber(number)) {
            System.out.println("Número de conta já existe!");
            return false;
        }

        SavingsAccount account = new SavingsAccount(number, 0.0);
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
        if (amount < 0) {
            System.out.println("Valor de depósito não pode ser negativo!");
            return false;
        }

        if (repository.existsByNumber(number) == false) {
            System.out.println("Número de conta não existe!");
            return false;
        }

        Account account = repository.findByNumber(number);
        account.setBalance(account.getBalance() + amount);

        if (account instanceof BonusAccount) {
            bonusCalculatorService.applyDepositPoints((BonusAccount) account, amount);
        }

        repository.save(account);
        return true;
    }

    public boolean withdraw(Long number, double amount) {
        if (amount < 0) {
            System.out.println("Valor de saque não pode ser negativo!");
            return false;
        }

        if (repository.existsByNumber(number) == false) {
            System.out.println("Número de conta não existe!");
            return false;
        }

        Account account = repository.findByNumber(number);
        Double value = account.getBalance() - amount;

        if (value < 0) {
          System.out.println("Saldo não pode ser negativo!");
          return false;
        }

        account.setBalance(value);
        repository.save(account);

        return true;
    }

    public boolean transfer(Long fromNumber, Long toNumber, double amount) {
        if (amount < 0) {
            System.out.println("Valor de transferência não pode ser negativo!");
            return false;
        }

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

        Double value = fromAccount.getBalance() - amount;

        if (value < 0) {
          System.out.println("Saldo não pode ser negativo!");
          return false;
        }

        fromAccount.setBalance(value);
        toAccount.setBalance(toAccount.getBalance() + amount);

        if (toAccount instanceof BonusAccount) {
            bonusCalculatorService.applyTransferPoints((BonusAccount) toAccount, amount);
        }

        repository.save(fromAccount);
        repository.save(toAccount);
        return true;
    }

    public void applyInterestToAllSavingsAccounts(double interestRate) {
        repository.findAllSavingsAccounts().forEach(account -> {
            account.applyInterest(interestRate);
            repository.save(account);
        });
    }

}



