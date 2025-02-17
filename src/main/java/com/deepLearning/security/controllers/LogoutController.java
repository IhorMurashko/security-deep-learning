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

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogoutController {

    private final RevokedTokenService revokedTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> tokens) {

        revokedTokenService.revokeToken(tokens);

        return ResponseEntity.ok("Logged successfully");

    }


}
