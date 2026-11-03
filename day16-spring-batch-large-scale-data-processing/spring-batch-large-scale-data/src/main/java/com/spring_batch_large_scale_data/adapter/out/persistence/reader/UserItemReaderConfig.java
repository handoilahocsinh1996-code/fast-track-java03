
package com.spring_batch_large_scale_data.adapter.out.persistence.reader;

import com.spring_batch_large_scale_data.adapter.out.persistence.entity.SourceUserEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserItemReaderConfig {

    @Bean
    public JpaPagingItemReader<SourceUserEntity> userItemReader(
            EntityManagerFactory sourceEntityManagerFactory) {

        JpaPagingItemReader<SourceUserEntity> reader = new JpaPagingItemReader<>();

        reader.setName("userItemReader");
        reader.setEntityManagerFactory(sourceEntityManagerFactory);
        reader.setQueryString(
                "SELECT u FROM SourceUserEntity u ORDER BY u.id");
        reader.setPageSize(1000);
        reader.setSaveState(true);

        return reader;
    }
}
