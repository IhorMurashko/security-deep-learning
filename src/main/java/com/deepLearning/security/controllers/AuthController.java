package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.jwt.JwtTokenManager;
import com.deepLearning.security.securityServices.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> signUp(@RequestBody AuthCredentials credentials) {

        boolean result = authService.registration(credentials);

        return
                result
                        ? new ResponseEntity<>(HttpStatus.CREATED)
                        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody AuthCredentials credentials) {


        Map<String, String> tokens = authService.authenticate(credentials);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {

        Map<String, String> tokens = jwtTokenManager.tokenManager(request);

        if (tokens != null && !tokens.isEmpty()) {
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }




}
