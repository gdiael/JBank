package com.ufrn.jbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.model.AccountType;
import com.ufrn.jbank.model.BonusAccount;

/** Dados da conta. O campo "bonus" só aparece quando a conta é do tipo BONUS. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountResponse(AccountType tipo, long numero, double saldo, Integer bonus) {

    public static AccountResponse from(Account account) {
        Integer bonus = (account instanceof BonusAccount bonusAccount)
                ? bonusAccount.getBonusPoints()
                : null;
        return new AccountResponse(AccountType.of(account), account.getNumber(), account.getBalance(), bonus);
    }
}
