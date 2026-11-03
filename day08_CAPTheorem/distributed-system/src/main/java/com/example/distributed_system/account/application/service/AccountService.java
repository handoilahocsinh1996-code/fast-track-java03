package com.example.distributed_system.account.application.service;

import com.example.distributed_system.account.application.port.in.AccountUseCase;
import com.example.distributed_system.account.application.port.out.AccountRepository;
import com.example.distributed_system.account.domain.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(String ownerName, BigDecimal initialBalance) {
        Account account = new Account(null, ownerName, initialBalance);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        from.withdraw(amount);
        to.deposit(amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }
}