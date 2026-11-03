package com.example.clean_architecture.domain.order.exception;

public class InvalidOrderException extends DomainException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
