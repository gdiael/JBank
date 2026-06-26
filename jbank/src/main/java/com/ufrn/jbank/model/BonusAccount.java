package com.ufrn.jbank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BonusAccount extends Account {
    private int bonusPoints;

    public BonusAccount(long number, double balance, int bonusPoints, double minAmount) {
        super(number, balance, minAmount);
        this.bonusPoints = bonusPoints;
    }

    public void addBonusPoints(int points) {
        this.bonusPoints += points;
        System.out.println("Conta [%d] recebeu %d pontos de bônus! Total de pontos: %d".formatted(getNumber(), points, bonusPoints));
    }
}
