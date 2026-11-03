package com.spring_batch_large_scale_data.application.port.out;

import com.spring_batch_large_scale_data.domain.model.User;

import java.util.List;

public interface UserMigrationPort {

    List<User> findUsersToMigrate(int page, int pageSize);

    void saveUsers(List<User> users);
}