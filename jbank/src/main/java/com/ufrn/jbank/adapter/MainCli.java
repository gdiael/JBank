package com.ufrn.jbank.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ufrn.jbank.service.AccountService;

@Component
public class MainCli {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountLoader accountLoader;

    public void runCli() {
        System.out.println("Bem-vindo ao JBank CLI!");

        // accountLoader.loadDummyAccounts();

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
                        long number = scanner.nextLong();
                        boolean res = accountService.createAccount(number);
                        if (res) {
                            System.out.println("Conta criada com sucesso!");
                        } else {
                            System.out.println("Não foi possível criar conta [%d]!".formatted(number));
                        }
                        break;

                    case 2:
                        System.out.println("Digite número da conta: ");
                        number = scanner.nextLong();
                        double balance = accountService.getBalance(number);
                        if (balance != Double.MIN_VALUE) {
                            System.out.println("Saldo da conta [%d]: R$ %.2f".formatted(number, balance));
                        }
                        break;

                    case 3:
                        System.out.println("Digite número da conta: ");
                        number = scanner.nextLong();
                        System.out.println("Digite valor do crédito: ");
                        double amount = scanner.nextDouble();
                        res = accountService.deposit(number, amount);
                        if (res) {
                            System.out.println("Crédito realizado com sucesso!");
                        } else {
                            System.out.println("Não foi possível realizar crédito na conta [%d]!".formatted(number));
                        }
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
