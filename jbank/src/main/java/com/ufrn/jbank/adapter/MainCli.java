package com.ufrn.jbank.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufrn.jbank.service.AccountService;

public class MainCli {
    
    @Autowired
    private AccountService accountService;

    public void runCli() {
        System.out.println("Bem-vindo ao JBank CLI!");
        try(Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("1 - Criar conta");
                System.out.println("2 - Consultar saldo");
                System.out.println("3 - Crédito");
                System.out.println("4 - Débito");
                System.out.println("5 - Transferência");

                int op = scanner.nextInt();

                switch (op) {
                    case 1:
                        System.out.println("Digite número da nova conta: ");
                        long accNumb = scanner.nextLong();
                        System.out.println("Não foi possível criar conta [%d]! Ainda não implementado!".formatted(accNumb));
                        break;
                
                    default:
                        System.out.println("Saindo!");
                        running = false;
                        break;
                }
                
            }
        }
        
    }

}
