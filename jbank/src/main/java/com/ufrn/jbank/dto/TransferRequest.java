package com.ufrn.jbank.dto;

/** Corpo da transferência: { "from": 123, "to": 456, "amount": 789 } */
public record TransferRequest(Long from, Long to, double amount) {
}
