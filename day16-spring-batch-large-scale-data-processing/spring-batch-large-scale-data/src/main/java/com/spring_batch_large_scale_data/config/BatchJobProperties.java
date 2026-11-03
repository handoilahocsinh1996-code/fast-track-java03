package com.spring_batch_large_scale_data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "migration.batch")
public class BatchJobProperties {

    private int chunkSize = 1000;

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than zero");
        }

        this.chunkSize = chunkSize;
    }
}