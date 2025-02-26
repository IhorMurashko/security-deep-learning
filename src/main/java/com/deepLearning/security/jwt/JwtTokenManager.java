package com.deepLearning.security.jwt;

import com.deepLearning.security.dto.TokensDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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
 * The result is returned as an object containing the new tokens - "accessToken" and optionally "refreshToken".
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
     * @param tokens an object containing the "refreshToken" with the current refresh token value.
     * @return an object containing the "accessToken" and, if refreshed, the "refreshToken" with the corresponding new tokens.
     */
    public TokensDto manageTokens(TokensDto tokens) {
        final String refreshToken = tokens.refreshToken();
        String newAccessToken = null;
        String newRefreshToken = null;

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            newAccessToken = jwtTokenProvider.generateAccessToken(user);

            if (jwtTokenProvider.isRefreshTokenExpiredSoon(refreshToken)) {
                newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
            }
        }

        return new TokensDto(newAccessToken,
                newRefreshToken == null
                        ? newRefreshToken
                        : tokens.refreshToken()
        );
    }
}
