package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.jwt.JwtTokenManager;
import com.deepLearning.security.securityServices.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Controller for user authentication and token management")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;

    @Operation(summary = "User registration", description = "Registers a new user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/sign-up")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<HttpStatus> signUp(@RequestBody AuthCredentials credentials) {
        boolean result = authService.registration(credentials);
        return result
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User authentication", description = "Authenticates a user and returns access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokensDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/sign-in")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<TokensDto> signIn(@RequestBody AuthCredentials credentials) {
        TokensDto tokens = authService.authenticate(credentials);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @Operation(summary = "Refresh token", description = "Refreshes access token using a valid refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New access token generated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokensDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> refreshToken(@RequestBody TokensDto tokens) {
        TokensDto tokensDto = jwtTokenManager.manageTokens(tokens);

        if (tokensDto != null) {
            return new ResponseEntity<>(tokensDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
