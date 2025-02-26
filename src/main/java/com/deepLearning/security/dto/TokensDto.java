package com.deepLearning.security.dto;

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
public record TokensDto(
        String accessToken,
        String refreshToken
) {
}
