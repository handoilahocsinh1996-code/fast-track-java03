package com.example.ddd_assignment.wallet.domain.model;

import com.example.ddd_assignment.wallet.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    @Test
    void shouldWithdrawMoneySuccessfully() {

        Wallet wallet = new Wallet(
                1L,
                new BigDecimal("1000"));

        wallet.withdrawMoney(
                new BigDecimal("300"));

        assertEquals(
                new BigDecimal("700"),
                wallet.getBalance());
    }

    @Test
    void shouldNotWithdrawWhenWalletIsLocked() {

        Wallet wallet = new Wallet(
                1L,
                new BigDecimal("1000"));

        wallet.lockWallet();

        assertThrows(
                DomainException.class,
                () -> wallet.withdrawMoney(
                        new BigDecimal("300")));
    }

    @Test
    void shouldNotWithdrawMoreThanBalance() {

        Wallet wallet = new Wallet(
                1L,
                new BigDecimal("1000"));

        assertThrows(
                DomainException.class,
                () -> wallet.withdrawMoney(
                        new BigDecimal("1500")));
    }

    @Test
    void shouldLockWalletSuccessfully() {

        Wallet wallet = new Wallet(
                1L,
                new BigDecimal("1000"));

        wallet.lockWallet();

        assertEquals(
                WalletStatus.LOCKED,
                wallet.getStatus());
    }

    @Test
    void shouldNotLockWalletTwice() {

        Wallet wallet = new Wallet(
                1L,
                new BigDecimal("1000"));

        wallet.lockWallet();

        assertThrows(
                DomainException.class,
                wallet::lockWallet);
    }
}