package com.example.ddd_assignment.wallet.domain.model;

import com.example.ddd_assignment.wallet.domain.exception.DomainException;

import java.math.BigDecimal;

public class Wallet {

    private final Long id;
    private BigDecimal balance;
    private WalletStatus status;

    public Wallet(Long id, BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Balance cannot be negative");
        }

        this.id = id;
        this.balance = balance;
        this.status = WalletStatus.ACTIVE;
    }

    public void withdrawMoney(BigDecimal amount) {

        if (status == WalletStatus.LOCKED) {
            throw new DomainException(
                    "Cannot withdraw money from a locked wallet");
        }

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(
                    "Withdrawal amount must be greater than zero");
        }

        if (amount.compareTo(balance) > 0) {
            throw new DomainException(
                    "Insufficient wallet balance");
        }

        balance = balance.subtract(amount);
    }

    public void lockWallet() {

        if (status == WalletStatus.LOCKED) {
            throw new DomainException(
                    "Wallet is already locked");
        }

        status = WalletStatus.LOCKED;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }
}