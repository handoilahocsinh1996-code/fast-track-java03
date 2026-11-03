package com.example.ddd_assignment.wallet.application.service;

import com.example.ddd_assignment.wallet.domain.model.Wallet;

import java.math.BigDecimal;

public class WalletService {

    public void withdraw(
            Wallet wallet,
            BigDecimal amount) {
        wallet.withdrawMoney(amount);
    }

    public void lock(
            Wallet wallet) {
        wallet.lockWallet();
    }
}