package com.spring_batch_large_scale_data.adapter.in.scheduler;

import com.spring_batch_large_scale_data.application.port.in.StartMigrationUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MigrationScheduler {

    private final StartMigrationUseCase startMigrationUseCase;

    public MigrationScheduler(StartMigrationUseCase startMigrationUseCase) {
        this.startMigrationUseCase = startMigrationUseCase;
    }

    @Scheduled(cron = "${migration.schedule.cron:0 0 0 * * *}")
    public void runScheduledMigration() {
        startMigrationUseCase.startMigration();
    }
}