package com.example.distributed_system.account.application.port.in;

import com.example.distributed_system.account.domain.model.Account;

import java.math.BigDecimal;

public interface AccountUseCase {

    Account createAccount(String ownerName, BigDecimal initialBalance);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}