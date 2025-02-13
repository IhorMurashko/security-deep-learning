package com.deepLearning.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtTokenManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public Map<String, String> tokenManager(Map<String, String> request) {
        final String refreshToken = request.get("refreshToken");
        Map<String, String> tokens = new HashMap<>();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            final String newAccessToken = jwtTokenProvider.generateAccessToken(user);

            tokens.put("accessToken", newAccessToken);


            if (jwtTokenProvider.isRefreshTokenExpiredSoon(refreshToken)) {
                String generatedRefreshToken = jwtTokenProvider.generateRefreshToken(user);
                tokens.put("refreshToken", generatedRefreshToken);
            }
        }
        return tokens;
    }
}
