package com.deepLearning.security.redis;

import com.deepLearning.security.dto.TokensDto;

/**
 * RevokedTokenService handles the revocation of JWT tokens by storing them in a Redis-based blacklist.
 * <p>
 * This service uses a {@code StringRedisTemplate} to persist revoked tokens with a time-to-live (TTL)
 * equal to the remaining lifetime of the token. It supports both access tokens and refresh tokens.
 * <p>
 * The main methods are:
 * <ul>
 *   <li>{@code revokeToken(TokenDto tokens)} - Revokes the provided access and refresh tokens by storing
 *       them in Redis with appropriate TTLs.</li>
 *   <li>{@code isTokenRevoked(String token)} - Checks if a given token is present in the Redis blacklist.</li>
 * </ul>
 *
 */
public interface RevokedTokenService {

    /**
     * Revokes the provided tokens by storing them in Redis with a TTL equal to their remaining lifetime.
     * <p>
     * The method expects a map containing the keys "refreshToken" and "accessToken". For each token, it calculates
     * the remaining time until expiration and stores the token in Redis with that TTL if the token is still valid.
     *
     * @param tokens a object containing tokens value (e.g., accessToken, refreshToken)
     */
    void revokeToken(TokensDto tokens);

    /**
     * Checks if a given token is revoked by verifying its existence in the Redis blacklist.
     *
     * @param token the JWT token to check for revocation
     * @return {@code true} if the token is revoked; {@code false} otherwise
     */
    boolean isTokenRevoked(String token);

}
