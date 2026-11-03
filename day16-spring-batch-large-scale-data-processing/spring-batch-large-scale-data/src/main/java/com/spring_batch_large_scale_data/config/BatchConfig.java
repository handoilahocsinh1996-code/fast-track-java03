
package com.spring_batch_large_scale_data.config;

import com.spring_batch_large_scale_data.adapter.out.persistence.entity.SourceUserEntity;
import com.spring_batch_large_scale_data.adapter.out.persistence.entity.TargetUserEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

        @Bean
        public Step migrateUsersStep(
                        JobRepository jobRepository,
                        @Qualifier("targetTransactionManager") PlatformTransactionManager transactionManager,
                        JpaPagingItemReader<SourceUserEntity> userItemReader,
                        ItemProcessor<SourceUserEntity, TargetUserEntity> userItemProcessor,
                        JdbcBatchItemWriter<TargetUserEntity> userItemWriter,
                        BatchJobProperties batchJobProperties) {

                return new StepBuilder("migrateUsersStep", jobRepository)
                                .<SourceUserEntity, TargetUserEntity>chunk(
                                                batchJobProperties.getChunkSize(),
                                                transactionManager)
                                .reader(userItemReader)
                                .processor(userItemProcessor)
                                .writer(userItemWriter)
                                .faultTolerant()
                                .skip(IllegalArgumentException.class)
                                .skipLimit(100)
                                .retry(org.springframework.dao.TransientDataAccessException.class)
                                .retryLimit(3)
                                .build();
        }

        @Bean
        public Job migrateUsersJob(
                        JobRepository jobRepository,
                        Step migrateUsersStep) {

                return new JobBuilder("migrateUsersJob", jobRepository)
                                .start(migrateUsersStep)
                                .build();
        }
}
