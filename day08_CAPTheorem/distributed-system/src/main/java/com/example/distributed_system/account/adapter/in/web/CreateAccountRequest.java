package com.example.distributed_system.account.adapter.in.web;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String ownerName,
        BigDecimal initialBalance) {
}