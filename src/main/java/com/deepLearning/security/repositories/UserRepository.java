package com.deepLearning.security.repositories;

import com.deepLearning.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * UserRepository is a repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link ListCrudRepository}, which provides CRUD operations for the {@code User} entity.
 * Additional query methods are defined to find, delete, and check the existence of a user by their username.
 * <p>
 * <b>Key Methods:</b>
 * <ul>
 *   <li>{@code Optional<User> findByUsername(String username)} - Retrieves a user by their username.</li>
 *   <li>{@code void deleteByUsername(String username)} - Deletes a user by their username.</li>
 *   <li>{@code boolean existsUserByUsername(String username)} - Checks if a user with the given username exists.</li>
 * </ul>
 * <p>
 * <b>Note:</b> It is assumed that the username is unique as defined by the {@code User} entity.
 * For more advanced use cases (e.g., pagination, sorting), consider extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Optional&lt;User&gt; userOpt = userRepository.findByUsername("john.doe");
 * if (userOpt.isPresent()) {
 *     // User found, proceed with business logic
 * }
 * </pre>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a {@link User} by their username.
     *
     * @param username the username to search for.
     * @return an {@link Optional} containing the {@code User} if found, or {@code Optional.empty()} if not.
     */
    Optional<User> findByUsername(String username);

    /**
     * Deletes a {@link User} identified by their username.
     *
     * @param username the username of the user to be deleted.
     */
    void deleteByUsername(String username);

    /**
     * Checks whether a {@link User} with the specified username exists.
     *
     * @param username the username to check for existence.
     * @return {@code true} if a user with the given username exists, {@code false} otherwise.
     */
    boolean existsUserByUsername(String username);
}
