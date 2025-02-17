package com.deepLearning.security.redis;

import com.deepLearning.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevokedTokenService {

    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public void revokeToken(Map<String, String> tokens) {
        final String refreshToken = tokens.get("refreshToken");
        final String accessToken = tokens.get("accessToken");

        long accessTokenExpiration = jwtTokenProvider.extractClaimFromToken(accessToken, claims ->
                claims.getExpiration().getTime());

        long accessTokenTTL = accessTokenExpiration - System.currentTimeMillis();

        if (accessTokenTTL > 0) {
            redisTemplate.opsForValue().set(accessToken, "access_token_revoked", accessTokenTTL, TimeUnit.MILLISECONDS);
            log.info("revoked access token: {}", accessTokenTTL);
        }





        long refreshTokenExpiration = jwtTokenProvider.extractClaimFromToken(refreshToken, claims ->
                claims.getExpiration().getTime());

        long refreshTokenTTL = refreshTokenExpiration - System.currentTimeMillis();

        if (refreshTokenTTL > 0) {
            redisTemplate.opsForValue().set(refreshToken, "refresh_token_revoked", refreshTokenTTL, TimeUnit.MILLISECONDS);
            log.info("revoked refresh token: {}", refreshTokenTTL);
        }


    }

    public boolean isTokenRevoked(String token) {
        return redisTemplate.hasKey(token);
    }

}
