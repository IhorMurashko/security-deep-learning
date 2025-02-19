package com.deepLearning.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenManager is responsible for managing JWT tokens, particularly for refreshing tokens.
 * <p>
 * This service uses the {@code JwtTokenProvider} to validate and generate JWT tokens, and the
 * {@code UserDetailsService} to load user details based on the username extracted from the refresh token.
 * <p>
 * The primary functionality provided by this class is to validate a given refresh token and, if valid,
 * generate a new access token. Additionally, if the refresh token is about to expire (e.g., within one day),
 * a new refresh token is also generated.
 * <p>
 * The result is returned as a map containing the new tokens, with keys "accessToken" and optionally "refreshToken".
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Map&lt;String, String&gt; request = new HashMap&lt;&gt;();
 * request.put("refreshToken", "your_refresh_token_here");
 * Map&lt;String, String&gt; tokens = jwtTokenManager.refreshTokens(request);
 * // tokens now contains a new access token and, if applicable, a new refresh token.
 * </pre>
 *
 * @see JwtTokenProvider for token operations.
 * @see UserDetailsService for retrieving user details.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class JwtTokenManager {

    /**
     * Provides JWT token operations such as token validation, extraction of claims, and token generation.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Loads user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Validates the provided refresh token and generates new tokens as necessary.
     * <p>
     * The method performs the following steps:
     * <ol>
     *   <li>Extracts the refresh token from the provided request map.</li>
     *   <li>If the refresh token is valid, extracts the username from the token and loads user details.</li>
     *   <li>Generates a new access token using the user details.</li>
     *   <li>If the refresh token is close to expiration (less than one day remaining), a new refresh token is also generated.</li>
     *   <li>Returns a map containing the new access token and, if applicable, the new refresh token.</li>
     * </ol>
     *
     * @param request a map containing the key "refreshToken" with the current refresh token value.
     * @return a map containing the key "accessToken" and, if refreshed, the key "refreshToken" with the corresponding new tokens.
     */
    public Map<String, String> manageTokens(Map<String, String> request) {
        final String refreshToken = request.get("refreshToken");
        Map<String, String> tokens = new HashMap<>();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            final String newAccessToken = jwtTokenProvider.generateAccessToken(user);
            tokens.put("accessToken", newAccessToken);

            if (jwtTokenProvider.isRefreshTokenExpiredSoon(refreshToken)) {
                String generatedRefreshToken = jwtTokenProvider.generateRefreshToken(user);
                tokens.put("refreshToken", generatedRefreshToken);
            }
        }
        return tokens;
    }
}
