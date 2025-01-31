package com.deepLearning.security.repositories;

import com.deepLearning.security.model.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends ListCrudRepository<UserEntity, String> {
}
