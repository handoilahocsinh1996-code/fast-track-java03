package com.spring_batch_large_scale_data.domain.model;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(
            Long id,
            String username,
            String email,
            String fullName,
            LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}