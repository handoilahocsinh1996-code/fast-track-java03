package com.example.clean_architecture.adapters.inbound.web;

import com.example.clean_architecture.domain.order.exception.InvalidOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrder(
            InvalidOrderException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", exception.getMessage()
                ));
    }
}
