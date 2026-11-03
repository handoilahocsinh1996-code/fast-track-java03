package com.example.distributed_system.account.adapter.out.persistence;

import com.example.distributed_system.account.application.port.out.AccountRepository;
import com.example.distributed_system.account.domain.model.Account;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountRepository repository;

    public AccountRepositoryAdapter(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {

        AccountJpaEntity entity = new AccountJpaEntity(
                account.getId(),
                account.getOwnerName(),
                account.getBalance());

        AccountJpaEntity saved = repository.save(entity);

        return new Account(
                saved.getId(),
                saved.getOwnerName(),
                saved.getBalance());
    }

    @Override
    public Optional<Account> findById(Long id) {

        return repository.findById(id)
                .map(entity -> new Account(
                        entity.getId(),
                        entity.getOwnerName(),
                        entity.getBalance()));
    }
}