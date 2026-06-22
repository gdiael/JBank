package com.ufrn.jbank.dto;

/** Corpo do rendimento: { "rate": 0.01 }  (taxa em fração: 0.01 = 1%) */
public record InterestRequest(double rate) {
}
