
package com.spring_batch_large_scale_data.adapter.out.persistence.processor;

import com.spring_batch_large_scale_data.adapter.out.persistence.entity.SourceUserEntity;
import com.spring_batch_large_scale_data.adapter.out.persistence.entity.TargetUserEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserItemProcessor
        implements ItemProcessor<SourceUserEntity, TargetUserEntity> {

    @Override
    public TargetUserEntity process(SourceUserEntity source) {
        if (source == null) {
            return null;
        }

        String username = normalize(source.getUsername());
        String email = normalize(source.getEmail());
        String fullName = normalize(source.getFullName());

        if (username == null || email == null) {
            throw new IllegalArgumentException(
                    "Username and email must not be blank");
        }

        return new TargetUserEntity(
                source.getId(),
                username,
                email,
                fullName,
                source.getCreatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
