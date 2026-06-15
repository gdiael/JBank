package com.ufrn.jbank.dto;

/** Resposta de GET /banco/conta/<id>/saldo */
public record BalanceResponse(long numero, double saldo) {
}
