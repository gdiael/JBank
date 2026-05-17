package com.ufrn.jbank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SavingsAccount extends Account {

  public SavingsAccount(Long number, double balance) {
    super(number, balance);
  }

  public void applyInterest(double interestRate) {
    double balance = getBalance();
    double interest = balance * interestRate;
    setBalance(balance + interest);
    System.out.println(
        "Rendimento aplicados na conta [%d]: %.2f Novo saldo: %.2f".formatted(getNumber(), interest, getBalance()));
  }

}
