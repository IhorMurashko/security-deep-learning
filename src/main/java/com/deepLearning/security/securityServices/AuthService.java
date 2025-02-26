package com.deepLearning.security.securityServices;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.model.User;

import java.util.Map;


/**
 * AuthService defines the contract for user authentication and registration operations.
 * <p>
 * The interface declares two primary methods:
 * <ul>
 *   <li>{@code authenticate(AuthCredentials credentials)} - Authenticates a user based on the provided credentials and returns a map containing the generated access and refresh tokens.</li>
 *   <li>{@code registration(AuthCredentials credentials)} - Registers a new user with the provided credentials. Returns {@code true} if the registration is successful, or throws an exception if the user already exists.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * AuthService authService = ...;
 * AuthCredentials credentials = new AuthCredentials("john.doe", "securePassword");
 * Map&lt;String, String&gt; tokens = authService.authenticate(credentials);
 * </pre>
 *
 * @see com.deepLearning.security.dto.AuthCredentials
 */
public interface AuthService {

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param credentials the authentication credentials containing username and password.
     * @return a {@link TokensDto} containing the generated access and refresh tokens.
     */
    TokensDto authenticate(AuthCredentials credentials);

    /**
     * Registers a new user with the provided credentials.
     *
     * @param credentials the registration credentials containing username and password.
     * @return {@code true} if the registration was successful.
     * @throws com.deepLearning.security.exceptions.UserAlreadyExist if a user with the given username already exists.
     */
    boolean registration(AuthCredentials credentials);
}