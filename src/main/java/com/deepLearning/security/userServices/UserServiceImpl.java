package com.deepLearning.security.userServices;

import com.deepLearning.security.model.User;
import com.deepLearning.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * UserServiceImpl provides a concrete implementation of the {@link UserService} interface,
 * as well as {@link org.springframework.security.core.userdetails.UserDetailsService} for loading user details during authentication.
 * <p>
 * It delegates CRUD operations to {@link UserRepository} and is responsible for basic user management, such as
 * saving, retrieving, and deleting user entities. The {@code loadUserByUsername} method is used by Spring Security
 * to retrieve user details based on the username.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * User user = userService.save(new User("john.doe", "encodedPassword", null, List.of("ROLE_USER")));
 * UserDetails userDetails = userService.loadUserByUsername("john.doe");
 * </pre>
 *
 * @see com.deepLearning.security.model.User
 * @see com.deepLearning.security.repositories.UserRepository
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * Repository for performing CRUD operations on User entities.
     */
    private final UserRepository userRepo;

    /**
     * Saves the provided user entity.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    @Override
    public User save(@NonNull User user) {
        return userRepo.save(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    @Override
    public Optional<User> findUserByUsername(@NonNull String username) {
        return userRepo.findByUsername(username);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    @Override
    public Optional<User> findUserById(@NonNull Long id) {
        return userRepo.findById(id);
    }

    /**
     * Deletes the user identified by the given ID.
     *
     * @param id the ID of the user to delete.
     */
    @Override
    public void deleteUserById(@NonNull Long id) {
        userRepo.deleteById(id);
    }

    /**
     * Deletes the user with the specified username.
     *
     * @param username the username of the user to delete.
     */
    @Override
    public void deleteByUsername(@NonNull String username) {
        userRepo.deleteByUsername(username);
    }

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username the username to check.
     * @return {@code true} if a user with the given username exists, {@code false} otherwise.
     */
    @Override
    public boolean isExistUsername(@NonNull String username) {
        return userRepo.existsUserByUsername(username);
    }

    /**
     * Loads the user by username for authentication purposes.
     * <p>
     * If the user is not found, a {@link UsernameNotFoundException} is thrown.
     *
     * @param username the username of the user to load.
     * @return a {@link UserDetails} object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
