package com.deepLearning.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * JwtTokenProvider is responsible for generating, validating, and extracting information from JWT tokens.
 * <p>
 * This component provides methods to generate both access tokens and refresh tokens for a given user,
 * validate tokens, extract the username (subject) from a token, and check whether a refresh token is nearing expiration.
 * <p>
 * The generated tokens include a custom claim "token_type" to distinguish between access tokens and refresh tokens.
 * Additionally, the access token includes the "authorities" claim to represent the user's roles or permissions.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * UserDetails user = userDetailsService.loadUserByUsername("john.doe");
 * String accessToken = jwtTokenProvider.generateAccessToken(user);
 * String refreshToken = jwtTokenProvider.generateRefreshToken(user);
 * </pre>
 *
 * <p>
 * <b>Note:</b> In a production environment, the secret key should be externalized in configuration
 * rather than hard-coded, and additional error handling and validation might be necessary.
 * </p>
 */
@Component
@Slf4j
public class JwtTokenProvider {

    /**
     * Secret key used for signing and verifying JWT tokens.
     * <p>
     * In a real-world application, this key should be injected from external configuration.
     */
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    /**
     * Generates a refresh token for the provided user details.
     * <p>
     * The token is valid for 30 days and includes a custom claim "token_type" with the value "refreshToken".
     *
     * @param userDetails the user details for whom the token is generated
     * @return a JWT refresh token as a String
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        // Refresh token validity period: 30 days
        long refreshTokenExpirationMs = 2592000000L;
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(now).expiration(expiryDate)
                .claim("token_type", "refreshToken")
                .signWith(key)
                .compact();
    }

    /**
     * Generates an access token for the provided user details.
     * <p>
     * The token is valid for 3 minutes and includes custom claims:
     * <ul>
     *   <li>"token_type" with the value "accessToken"</li>
     *   <li>"authorities" containing the user's granted authorities</li>
     * </ul>
     *
     * @param userDetails the user details for whom the token is generated
     * @return a JWT access token as a String
     */
    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        // Access token validity period: 3 minutes
        long accessTokenExpirationMs = 180000;
        Date expirationDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(now).expiration(expirationDate)
                .claim("token_type", "accessToken")
                .claim("authorities", userDetails.getAuthorities())
                .signWith(key)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     * <p>
     * The token is considered valid if it is non-null, non-empty, and can be successfully parsed with the secret key.
     * If the token is invalid or cannot be parsed, a JwtException is thrown.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid, {@code false} if it is null or empty
     * @throws JwtException if the token is invalid
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    /**
     * Extracts the username (subject) from the provided JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username (subject) contained in the token
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Checks whether the given refresh token is close to expiration.
     * <p>
     * This method extracts the expiration date from the token and compares the remaining time
     * with one day (86400000 milliseconds). If the remaining time is less than one day,
     * the method returns {@code true}.
     *
     * @param refreshToken the refresh token to check
     * @return {@code true} if the refresh token will expire in less than one day, {@code false} otherwise
     */
    public boolean isRefreshTokenExpiredSoon(String refreshToken) {
        Date expirationDate = extractClaimFromToken(refreshToken, Claims::getExpiration);
        if (expirationDate == null) {
            return true;
        }
        long remainingMillis = expirationDate.getTime() - System.currentTimeMillis();
        long oneDayInMillis = 86400000L; // 24 hours
        return remainingMillis < oneDayInMillis;
    }

    /**
     * Extracts a specific claim from the JWT token using a provided function.
     * <p>
     * This method parses the token to retrieve its claims and then applies the provided function to extract
     * a particular piece of information.
     * <p>
     * <b>Note:</b> If the token is malformed, an error is logged and the function is applied to a null claim,
     * which may lead to a {@code NullPointerException}. Consider adding additional null checks as needed.
     *
     * @param token    the JWT token from which to extract the claim
     * @param function a function that processes the {@link Claims} and returns a desired value
     * @param <T>      the type of the value to be returned
     * @return the value extracted from the token's claims
     */
    public <T> T extractClaimFromToken(String token, Function<Claims, T> function) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token", ex);
        }
        return function.apply(claims);
    }
}