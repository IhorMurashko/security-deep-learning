package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.jwt.JwtTokenProvider;
import com.deepLearning.security.redis.RevokedTokenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 */
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Logout Controller", description = "API for revoking tokens upon user logout")
public class LogoutController {

    /**
     * Service responsible for revoking tokens by storing them in a blacklist (e.g., in Redis).
     */
    private final RevokedTokenServiceImpl revokedTokenServiceImpl;

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
     * @param tokens a object containing token values (e.g., accessToken, refreshToken)
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @Operation(summary = "User Logout", description = "Revokes tokens to prevent further authentication with them")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Invalid token format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - token is missing or invalid")
    })
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokensDto tokens) {
        revokedTokenServiceImpl.revokeToken(tokens);
        return ResponseEntity.ok("Logged out successfully");
    }
}
