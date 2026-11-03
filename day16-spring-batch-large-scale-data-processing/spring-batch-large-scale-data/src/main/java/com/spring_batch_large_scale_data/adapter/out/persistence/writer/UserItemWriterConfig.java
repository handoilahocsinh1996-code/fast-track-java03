
package com.spring_batch_large_scale_data.adapter.out.persistence.writer;

import com.spring_batch_large_scale_data.adapter.out.persistence.entity.TargetUserEntity;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class UserItemWriterConfig {

    @Bean
    public JdbcBatchItemWriter<TargetUserEntity> userItemWriter(
            @Qualifier("targetDataSource") DataSource targetDataSource) {

        return new JdbcBatchItemWriterBuilder<TargetUserEntity>()
                .dataSource(targetDataSource)
                .sql("""
                        INSERT INTO users (
                            id, username, email, full_name, created_at
                        )
                        VALUES (
                            :id, :username, :email, :fullName, :createdAt
                        )
                        ON DUPLICATE KEY UPDATE
                            username = VALUES(username),
                            email = VALUES(email),
                            full_name = VALUES(full_name),
                            created_at = VALUES(created_at)
                        """)
                .beanMapped()
                .assertUpdates(false)
                .build();
    }
}
