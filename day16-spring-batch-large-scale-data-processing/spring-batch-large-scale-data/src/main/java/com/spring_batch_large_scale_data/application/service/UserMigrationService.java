package com.spring_batch_large_scale_data.application.service;

import com.spring_batch_large_scale_data.application.port.in.StartMigrationUseCase;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserMigrationService implements StartMigrationUseCase {

    private final JobLauncher jobLauncher;
    private final Job migrateUsersJob;

    public UserMigrationService(
            JobLauncher jobLauncher,
            @Qualifier("migrateUsersJob") Job migrateUsersJob) {
        this.jobLauncher = jobLauncher;
        this.migrateUsersJob = migrateUsersJob;
    }

    @Override
    public Long startMigration() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("requestedAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(
                    migrateUsersJob,
                    jobParameters);

            return jobExecution.getId();

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Failed to start user migration job",
                    exception);
        }
    }
}