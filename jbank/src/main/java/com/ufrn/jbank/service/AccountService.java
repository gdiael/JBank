package com.ufrn.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufrn.jbank.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository repository; // aqui a instância do repositório é injetada pelo spring

}
