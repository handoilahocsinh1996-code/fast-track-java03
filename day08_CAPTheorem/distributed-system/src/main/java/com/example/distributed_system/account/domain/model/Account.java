package com.example.distributed_system.account.domain.model;

import java.math.BigDecimal;

public class Account {

    private Long id;
    private String ownerName;
    private BigDecimal balance;

    public Account(Long id, String ownerName, BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        balance = balance.subtract(amount);
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}