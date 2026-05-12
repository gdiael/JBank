package com.ufrn.jbank.adapter;

import java.util.InputMismatchException;
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

        accountLoader.loadDummyAccounts();

        try(Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("1 - Criar conta simples");
                System.out.println("2 - Criar conta de bônus");
                System.out.println("3 - Criar conta poupança");
                System.out.println("4 - Consultar saldo");
                System.out.println("5 - Crédito");
                System.out.println("6 - Débito");
                System.out.println("7 - Transferência");
                System.out.println("8 - Aplicar juros em todas as contas poupança");
                System.out.println("0 - Sair");

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
                        System.out.println("Digite número da nova conta de bônus: ");
                        number = scanner.nextLong();
                        res = accountService.createBonusAccount(number);
                        if (res) {
                            System.out.println("Conta de bônus criada com sucesso!");
                        } else {
                            System.out.println("Não foi possível criar conta de bônus [%d]!".formatted(number));
                        }
                        break;

                    case 3:
                        System.out.println("Digite número da nova conta poupança: ");
                        number = scanner.nextLong();
                        res = accountService.createSavingsAccount(number);
                        if (res) {
                            System.out.println("Conta poupança criada com sucesso!");
                        } else {
                            System.out.println("Não foi possível criar conta poupança [%d]!".formatted(number));
                        }
                        break;

                    case 4:
                        System.out.println("Digite número da conta: ");
                        number = scanner.nextLong();
                        double balance = accountService.getBalance(number);
                        if (balance != Double.MIN_VALUE) {
                            System.out.println("Saldo da conta [%d]: R$ %.2f".formatted(number, balance));
                        }
                        break;

                    case 5:
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

                    case 6:
                        System.out.println("Digite número da conta: ");
                        number = scanner.nextLong();
                        System.out.println("Digite valor do débito: ");
                        amount = scanner.nextDouble();
                        res = accountService.withdraw(number, amount);
                        if (res) {
                            System.out.println("Débito realizado com sucesso!");
                        } else {
                            System.out.println("Não foi possível realizar débito na conta [%d]!".formatted(number));
                        }
                        break;

                    case 7:
                        System.out.println("Digite número da conta de origem: ");
                        long fromNumber = scanner.nextLong();
                        System.out.println("Digite número da conta de destino: ");
                        long toNumber = scanner.nextLong();
                        System.out.println("Digite valor da transferência: ");
                        amount = scanner.nextDouble();
                        res = accountService.transfer(fromNumber, toNumber, amount);
                        if (res) {
                            System.out.println("Transferência realizada com sucesso!");
                        } else {
                            System.out.println("Não foi possível realizar transferência da conta [%d] para conta [%d]!".formatted(fromNumber, toNumber));
                        }
                        break;

                    case 8:
                        System.out.println("Digite a taxa de juros  (valor em porcentagem, sem o %): ");
                        double interestRate = scanner.nextDouble();
                        accountService.applyInterestToAllSavingsAccounts(interestRate / 100.0);
                        System.out.println("Juros aplicados com sucesso!");
                        break;

                    default:
                        System.out.println("Saindo!");
                        running = false;
                        break;
                }

            }
        } catch (InputMismatchException e) {

      System.out.println("Entrada inválida! Encerrando.");
      return;
    }

    }

}


