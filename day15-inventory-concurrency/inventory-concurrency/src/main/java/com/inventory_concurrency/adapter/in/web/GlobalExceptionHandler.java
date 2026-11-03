package com.inventory_concurrency.adapter.in.web;

import com.inventory_concurrency.application.service.InventoryNotFoundException;
import com.inventory_concurrency.application.service.InventoryVersionConflictException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            InventoryNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler({
            InventoryVersionConflictException.class,
            OptimisticLockingFailureException.class
    })
    public ResponseEntity<Map<String, String>> handleConflict(
            RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", message));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequest(
            Exception exception) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Invalid request"));
    }
}