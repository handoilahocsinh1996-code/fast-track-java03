package com.example.distributed_system.account.adapter.in.web;

import com.example.distributed_system.account.application.port.in.AccountUseCase;
import com.example.distributed_system.account.domain.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountUseCase accountUseCase;

    public AccountController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @PostMapping
    public ResponseEntity<Account> create(
            @RequestBody CreateAccountRequest request) {
        Account account = accountUseCase.createAccount(
                request.ownerName(),
                request.initialBalance());

        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> transfer(
            @PathVariable Long id,
            @RequestBody TransferRequest request) {
        accountUseCase.transfer(
                id,
                request.toAccountId(),
                request.amount());

        return ResponseEntity.ok().build();
    }
}