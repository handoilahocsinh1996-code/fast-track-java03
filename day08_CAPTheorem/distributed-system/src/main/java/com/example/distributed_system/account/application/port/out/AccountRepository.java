package com.example.distributed_system.account.application.port.out;

import com.example.distributed_system.account.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(Long id);
}