package com.deepLearning.security.redis;

import com.deepLearning.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RevokedTokenService handles the revocation of JWT tokens by storing them in a Redis-based blacklist.
 * <p>
 * This service uses a {@code StringRedisTemplate} to persist revoked tokens with a time-to-live (TTL)
 * equal to the remaining lifetime of the token. It supports both access tokens and refresh tokens.
 * <p>
 * The main methods are:
 * <ul>
 *   <li>{@code revokeToken(Map<String, String> tokens)} - Revokes the provided access and refresh tokens by storing
 *       them in Redis with appropriate TTLs.</li>
 *   <li>{@code isTokenRevoked(String token)} - Checks if a given token is present in the Redis blacklist.</li>
 * </ul>
 * <p>
 * <b>Note:</b> In a production environment, consider using a dedicated DTO instead of a raw {@code Map<String, String>}
 * for better type safety and clarity.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Map&lt;String, String&gt; tokens = new HashMap&lt;&gt;();
 * tokens.put("accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
 * tokens.put("refreshToken", "def50200a1b2...");
 * revokedTokenService.revokeToken(tokens);
 * boolean revoked = revokedTokenService.isTokenRevoked("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RevokedTokenService {

    /**
     * Redis template for performing string-based operations.
     */
    private final StringRedisTemplate redisTemplate;

    /**
     * JwtTokenProvider provides methods for validating and extracting claims from JWT tokens.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Revokes the provided tokens by storing them in Redis with a TTL equal to their remaining lifetime.
     * <p>
     * The method expects a map containing the keys "refreshToken" and "accessToken". For each token, it calculates
     * the remaining time until expiration and stores the token in Redis with that TTL if the token is still valid.
     *
     * @param tokens a map containing token values with keys "refreshToken" and "accessToken"
     */
    public void revokeToken(Map<String, String> tokens) {
        final String refreshToken = tokens.get("refreshToken");
        final String accessToken = tokens.get("accessToken");

        // Revoke access token
        if (accessToken != null) {
            long accessTokenExpiration = jwtTokenProvider.extractClaimFromToken(accessToken, claims ->
                    claims.getExpiration().getTime());
            long accessTokenTTL = accessTokenExpiration - System.currentTimeMillis();
            if (accessTokenTTL > 0) {
                redisTemplate.opsForValue().set(accessToken, "access_token_revoked", accessTokenTTL, TimeUnit.MILLISECONDS);
                log.info("Revoked access token with TTL: {} ms", accessTokenTTL);
            }
        } else {
            log.warn("Access token is missing from the revoke request");
        }

        // Revoke refresh token
        if (refreshToken != null) {
            long refreshTokenExpiration = jwtTokenProvider.extractClaimFromToken(refreshToken, claims ->
                    claims.getExpiration().getTime());
            long refreshTokenTTL = refreshTokenExpiration - System.currentTimeMillis();
            if (refreshTokenTTL > 0) {
                redisTemplate.opsForValue().set(refreshToken, "refresh_token_revoked", refreshTokenTTL, TimeUnit.MILLISECONDS);
                log.info("Revoked refresh token with TTL: {} ms", refreshTokenTTL);
            }
        } else {
            log.warn("Refresh token is missing from the revoke request");
        }
    }

    /**
     * Checks if a given token is revoked by verifying its existence in the Redis blacklist.
     *
     * @param token the JWT token to check for revocation
     * @return {@code true} if the token is revoked; {@code false} otherwise
     */
    public boolean isTokenRevoked(String token) {
        return redisTemplate.hasKey(token);
    }
}