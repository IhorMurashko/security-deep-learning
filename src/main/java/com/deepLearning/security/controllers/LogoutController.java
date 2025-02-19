package com.deepLearning.security.controllers;

import com.deepLearning.security.jwt.JwtTokenProvider;
import com.deepLearning.security.redis.RevokedTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * LogoutController provides an endpoint for revoking user tokens during logout.
 * <p>
 * This controller exposes a POST endpoint at {@code /api/log/logout} that accepts token data
 * (typically containing an access token and/or a refresh token) in JSON format. Upon receiving the tokens,
 * the controller calls the {@code revokeToken} method of {@code RevokedTokenService} to add the tokens to
 * a blacklist. This prevents further use of the tokens for authentication.
 * <p>
 * The controller is designed for use in stateless RESTful applications using JWT-based security,
 * where explicit token revocation is necessary during logout.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * POST /api/log/logout
 * {
 *   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "refreshToken": "def50200a1b2..."
 * }
 * </pre>
 *
 * @see RevokedTokenService for token revocation logic.
 * @see JwtTokenProvider for token-related operations.
 */
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogoutController {

    /**
     * Service responsible for revoking tokens by storing them in a blacklist (e.g., in Redis).
     */
    private final RevokedTokenService revokedTokenService;

    /**
     * Utility for JWT token operations. Although injected here, it may be used for
     * additional token-related logic if needed.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Revokes the provided tokens by adding them to the blacklist.
     * <p>
     * This endpoint expects a JSON payload containing tokens to be revoked.
     * The tokens are processed by {@code revokedTokenService.revokeToken}, which handles
     * storing the tokens with appropriate expiration.
     *
     * @param tokens a map containing token key-value pairs (e.g., accessToken, refreshToken)
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> tokens) {
        revokedTokenService.revokeToken(tokens);
        return ResponseEntity.ok("Logged successfully");
    }
}