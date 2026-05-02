package com.ufrn.jbank.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ufrn.jbank.model.Account;

@Repository
public class AccountRepository {

    private Map<Long, Account> accounts = new HashMap<>();
    
    public void save(Account account) {
        accounts.put(account.getNumber(), account);
    }

    public Account findByNumber(Long number) {
        return accounts.get(number);
    }

    public boolean existsByNumber(Long number) {
        return accounts.containsKey(number);
    }
    
}
