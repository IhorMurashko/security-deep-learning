package com.deepLearning.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

/**
 * AuthCredentials is a simple data transfer object (DTO) that represents
 * user authentication credentials.
 * <p>
 * This record holds two non-null fields:
 * <ul>
 *   <li><b>username</b> - the identifier of the user attempting to authenticate.</li>
 *   <li><b>password</b> - the user's password.</li>
 * </ul>
 * <p>
 * Both fields are marked as non-null to ensure that no null values are provided during instantiation.
 * This class is immutable by design, making it a perfect candidate for use as a DTO in a stateless RESTful security context.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * AuthCredentials credentials = new AuthCredentials("john.doe", "securePassword123");
 * </pre>
 */
@Schema(description = "DTO containing user authentication credentials (username and password)")
public record AuthCredentials(
        @NonNull
        @Schema(description = "The username of the user attempting to authenticate", example = "john.doe")
        String username,

        @NonNull
        @Schema(description = "The user's password", example = "securePassword123")
        String password
) {
}
