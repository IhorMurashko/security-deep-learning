package com.deepLearning.security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/home")
public class HomeController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String getUser() {
        return "Hello World: " + LocalDateTime.now().toString();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin() {
        return "Hello World: " + LocalDateTime.now().toString();
    }


}
