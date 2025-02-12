package com.deepLearning.security.dto;


import lombok.NonNull;

public record AuthCredentials(
        @NonNull String username,
        @NonNull String password
) {

}
