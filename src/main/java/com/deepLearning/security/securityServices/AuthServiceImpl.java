package com.deepLearning.security.securityServices;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.exceptions.UserAlreadyExist;
import com.deepLearning.security.jwt.JwtTokenProvider;
import com.deepLearning.security.model.Roles;
import com.deepLearning.security.model.User;
import com.deepLearning.security.userServices.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


/**
 * AuthServiceImpl implements the {@link com.deepLearning.security.securityServices.AuthService} interface and provides
 * concrete methods for user authentication and registration.
 * <p>
 * The implementation utilizes:
 * <ul>
 *   <li>{@code UserService} for user persistence and existence checks.</li>
 *   <li>{@code PasswordEncoder} to encode user passwords.</li>
 *   <li>{@code UserDetailsService} to load user-specific data during authentication.</li>
 *   <li>{@code JwtTokenProvider} to generate JWT access and refresh tokens.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * AuthCredentials credentials = new AuthCredentials("john.doe", "securePassword");
 * Map&lt;String, String&gt; tokens = authService.authenticate(credentials);
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * Service for managing user entities.
     */
    private final UserService userService;

    /**
     * Encoder for hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service for loading user details during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Provider for generating JWT access and refresh tokens.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticates a user using the provided credentials and generates JWT tokens.
     * <p>
     * This method loads the user by username, generates an access token and a refresh token,
     * and returns them in an immutable map.
     *
     * @param credentials the authentication credentials (username and password).
     * @return a {@link TokensDto} containing the generated access and refresh tokens.
     */
    @Override
    public TokensDto authenticate(@NonNull AuthCredentials credentials) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.username());
        final String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Registers a new user with the provided credentials.
     * <p>
     * The method checks if a user with the specified username already exists.
     * If the user exists, a {@link com.deepLearning.security.exceptions.UserAlreadyExist} exception is thrown.
     * Otherwise, a new user is saved with the password encoded and a default role assigned.
     *
     * @param credentials the registration credentials (username and password).
     * @return {@code true} if the registration is successful.
     * @throws com.deepLearning.security.exceptions.UserAlreadyExist if a user with the given username already exists.
     */
    @Override
    public boolean registration(@NonNull AuthCredentials credentials) {
        boolean isExistUsername = userService.isExistUsername(credentials.username());
        if (isExistUsername) {
            throw new UserAlreadyExist(String.format("User with username %s already exists", credentials.username()));
        } else {
            // Instead of double-brace initialization, use Collections.singletonList for better performance.
            userService.save(new User(
                    credentials.username(),
                    passwordEncoder.encode(credentials.password()),
                    null,
                    Collections.singletonList(Roles.ROLE_USER.name())
            ));
            return true;
        }
    }
}
