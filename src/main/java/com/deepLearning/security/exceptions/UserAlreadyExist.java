package com.deepLearning.security.exceptions;


/**
 * UserAlreadyExist is a custom RuntimeException that is thrown when an attempt is made to register
 * a user that already exists in the system.
 * <p>
 * This exception indicates that the operation could not be completed because a user with the provided
 * identifier (e.g., username or email) is already present.
 * <p>
 * It is an unchecked exception (extending {@code RuntimeException}), so it does not require explicit
 * handling or declaration in method signatures.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * if (userService.exists(username)) {
 *     throw new UserAlreadyExist("User with username " + username + " already exists");
 * }
 * </pre>
 */
public class UserAlreadyExist extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExist exception with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public UserAlreadyExist(String message) {
        super(message);
    }
}