package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.securityServices.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> signIn(@RequestBody AuthCredentials credentials) {

        boolean result = authService.registration(credentials);

        return
                result
                        ? new ResponseEntity<>(HttpStatus.CREATED)
                        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signUp(@RequestBody AuthCredentials credentials) {


        String token = authService.authenticate(credentials);

        return new ResponseEntity<>(token, HttpStatus.OK);


    }


}
