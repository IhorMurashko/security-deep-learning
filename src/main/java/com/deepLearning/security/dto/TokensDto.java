package com.deepLearning.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data transfer object (DTO) representing authentication tokens.
 * <p>
 * This record encapsulates an access token and a refresh token,
 * which are issued upon successful authentication.
 * The access token is used for authorizing API requests,
 * while the refresh token allows obtaining a new access token
 * without requiring the user to log in again.
 *
 * @param accessToken  the JWT access token used for authentication.
 * @param refreshToken the JWT refresh token used for generating new access tokens.
 */
@Schema(description = "DTO containing authentication tokens (access and refresh)")
public record TokensDto(
        @Schema(description = "JWT access token used for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "JWT refresh token used for obtaining a new access token", example = "def50200a1b2...")
        String refreshToken
) {
}
