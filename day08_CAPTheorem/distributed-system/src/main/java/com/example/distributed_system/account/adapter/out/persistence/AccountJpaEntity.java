package com.example.distributed_system.account.adapter.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected AccountJpaEntity() {
    }

    public AccountJpaEntity(
            Long id,
            String ownerName,
            BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
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