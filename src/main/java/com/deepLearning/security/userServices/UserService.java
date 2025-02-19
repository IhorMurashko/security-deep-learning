package com.deepLearning.security.userServices;

import com.deepLearning.security.model.User;

import java.util.Optional;

/**
 * UserService defines the contract for managing user entities.
 * <p>
 * It provides methods for saving a user, retrieving a user by username or ID,
 * deleting a user by ID or username, and checking if a user exists by username.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Optional&lt;User&gt; userOpt = userService.findUserByUsername("john.doe");
 * if(userOpt.isPresent()) {
 *     // Process user
 * }
 * </pre>
 *
 * @see com.deepLearning.security.model.User
 */
public interface UserService {

    /**
     * Saves the given user.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    User save(User user);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<User> findUserById(Long id);

    /**
     * Deletes the user identified by the given ID.
     *
     * @param id the ID of the user to delete.
     */
    void deleteUserById(Long id);

    /**
     * Deletes the user with the specified username.
     *
     * @param username the username of the user to delete.
     */
    void deleteByUsername(String username);

    /**
     * Checks whether a user with the specified username exists.
     *
     * @param username the username to check.
     * @return {@code true} if a user with the username exists, {@code false} otherwise.
     */
    boolean isExistUsername(String username);
}
