package com.ufrn.jbank.model;

/** Tipos de conta suportados pelo cadastro e expostos na consulta. */
public enum AccountType {
  SIMPLES,
  BONUS,
  POUPANCA;

  public static AccountType of(Account account) {
    if (account instanceof BonusAccount) {
      return BONUS;
    }
    if (account instanceof SavingsAccount) {
      return POUPANCA;
    }
    return SIMPLES;
  }
}
