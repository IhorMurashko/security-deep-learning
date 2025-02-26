package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.jwt.JwtTokenManager;
import com.deepLearning.security.securityServices.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * AuthController is a REST controller responsible for handling authentication-related endpoints.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *   <li><b>Sign-up</b> - to register a new user using provided credentials.</li>
 *   <li><b>Sign-in</b> - to authenticate a user and return a set of tokens (e.g., access and refresh tokens).</li>
 *   <li><b>Refresh token</b> - to issue new tokens when the access token expires using a valid refresh token.</li>
 * </ul>
 * <p>
 * The endpoints are mapped under the base path <code>/api/auth</code> and use HTTP POST methods.
 * The controller leverages the {@code AuthService} for registration and authentication logic,
 * and {@code JwtTokenManager} for token management operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Service handling user registration and authentication logic.
     */
    private final AuthService authService;

    /**
     * Manager responsible for JWT token operations such as generating and refreshing tokens.
     */
    private final JwtTokenManager jwtTokenManager;

    /**
     * Handles user registration.
     * <p>
     * Accepts user credentials in the request body and attempts to register the user.
     * Returns HTTP status 201 (Created) if registration is successful,
     * or HTTP status 400 (Bad Request) if registration fails.
     *
     * @param credentials the authentication credentials (e.g., username and password)
     * @return ResponseEntity with appropriate HTTP status code
     */
    @PostMapping("/sign-up")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<HttpStatus> signUp(@RequestBody AuthCredentials credentials) {
        boolean result = authService.registration(credentials);
        return result
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles user authentication (sign-in).
     * <p>
     * Accepts user credentials in the request body, and if authentication is successful,
     * returns a map containing the generated tokens (e.g., access and refresh tokens) with HTTP status 200 (OK).
     *
     * @param credentials the authentication credentials (e.g., username and password)
     * @return ResponseEntity containing an object with tokens and HTTP status 200 (OK)
     */
    @PostMapping("/sign-in")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<TokensDto> signIn(@RequestBody AuthCredentials credentials) {
        TokensDto tokens = authService.authenticate(credentials);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    /**
     * Handles token refresh requests.
     * <p>
     * Accepts a request containing the refresh token, and if the refresh token is valid,
     * returns a new set of tokens with HTTP status 200 (OK). Otherwise, returns HTTP status 401 (Unauthorized)
     * with an error message.
     *
     * @param tokens an object containing the refresh token (expected key is defined in the token manager)
     * @return ResponseEntity containing new tokens if refresh is successful, or an error message if not.
     */
    @PostMapping("/refresh-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> refreshToken(@RequestBody TokensDto tokens) {
        TokensDto tokensDto = jwtTokenManager.manageTokens(tokens);

        if (tokensDto != null ) {
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
