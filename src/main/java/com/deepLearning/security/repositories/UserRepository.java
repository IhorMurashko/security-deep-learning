package com.deepLearning.security.repositories;

import com.deepLearning.security.model.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {


    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsUserByUsername(String username);
}
