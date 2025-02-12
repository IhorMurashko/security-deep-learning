package com.deepLearning.security.userServices;

import com.deepLearning.security.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);

    void deleteUserById(Long id);

    void deleteByUsername(String username);

    boolean isExistUsername(String username);

}
