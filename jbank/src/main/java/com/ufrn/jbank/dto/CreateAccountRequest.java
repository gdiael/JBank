package com.ufrn.jbank.dto;

import com.ufrn.jbank.model.AccountType;

/** Corpo do POST /banco/conta/  (balance é ignorado para contas BONUS). */
public record CreateAccountRequest(AccountType type, Long number, Double balance) {
}
