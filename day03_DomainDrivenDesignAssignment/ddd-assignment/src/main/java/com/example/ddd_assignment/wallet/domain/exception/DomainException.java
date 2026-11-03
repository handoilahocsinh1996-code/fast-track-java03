package com.example.ddd_assignment.wallet.domain.exception;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}