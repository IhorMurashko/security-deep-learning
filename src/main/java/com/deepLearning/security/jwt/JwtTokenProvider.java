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

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey key = Jwts.SIG.HS256.key().build();


    public String generateRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        // Срок действия refresh-токена (например, 30 дней)
        long refreshTokenExpirationMs = 2592000000L;
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);


        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("token_type", "refresh_token")
                .signWith(key)
                .compact();
    }


    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        // Срок действия access-токена (например, 1 час)
        long accessTokenExpirationMs = 3600000;
        Date expirationDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expirationDate)
                .claim("authorities", userDetails.getAuthorities())
                .signWith(key)
                .compact();
    }


    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token");
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isRefreshTokenExpiredSoon(String refreshToken) {

        Date expirationDate = extractClaimFromToken(refreshToken, Claims::getExpiration);
        if (expirationDate == null) {
            return true;
        }
        long remainingMillis = expirationDate.getTime() - System.currentTimeMillis();
        long oneDayInMillis = 86400000L; // 24 часа
        return remainingMillis < oneDayInMillis;
    }


    public <T> T extractClaimFromToken(String token, Function<Claims, T> function) {
        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        }
        return function.apply(claims);
    }

}