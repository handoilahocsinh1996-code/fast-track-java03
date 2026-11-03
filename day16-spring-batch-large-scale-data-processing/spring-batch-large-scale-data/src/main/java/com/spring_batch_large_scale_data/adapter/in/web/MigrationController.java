package com.spring_batch_large_scale_data.adapter.in.web;

import com.spring_batch_large_scale_data.application.port.in.StartMigrationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/migrations")
public class MigrationController {

    private final StartMigrationUseCase startMigrationUseCase;

    public MigrationController(StartMigrationUseCase startMigrationUseCase) {
        this.startMigrationUseCase = startMigrationUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> startMigration() {
        Long executionId = startMigrationUseCase.startMigration();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "message", "Migration job started",
                        "executionId", executionId));
    }
}