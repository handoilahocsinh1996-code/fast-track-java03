
package com.example.day11_redis_leaderboard.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                message
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleInvalidRequestBody(
            HttpMessageNotReadableException exception
    ) {
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                "The request body is missing or contains invalid JSON."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid parameter",
                "Parameter '" + exception.getName() + "' has an invalid value."
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(
            ResponseStatusException exception
    ) {
        HttpStatus status = HttpStatus.valueOf(
                exception.getStatusCode().value()
        );

        String message = exception.getReason() != null
                ? exception.getReason()
                : status.getReasonPhrase();

        return buildProblemDetail(status, "Request failed", message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(
            Exception exception
    ) {
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred."
        );
    }

    private ResponseEntity<ProblemDetail> buildProblemDetail(
            HttpStatus status,
            String title,
            String detail
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("about:blank"));

        return ResponseEntity.status(status).body(problem);
    }
}