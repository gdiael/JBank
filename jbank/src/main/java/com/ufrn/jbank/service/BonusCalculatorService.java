package com.ufrn.jbank.service;

import org.springframework.stereotype.Service;

import com.ufrn.jbank.model.BonusAccount;

@Service
public class BonusCalculatorService {
    
    public int calculateDepositPoints(double value) {
        return (int) (value / 100.0);
    }

    public int calculateTransferPoints(double value) {
        return (int) (value / 150.0);
    }

    public void applyDepositPoints(BonusAccount account, double value) {
        int points = calculateDepositPoints(value);
        account.addBonusPoints(points);
    }

    public void applyTransferPoints(BonusAccount account, double value) {
        int points = calculateTransferPoints(value);
        account.addBonusPoints(points);
    }

}
