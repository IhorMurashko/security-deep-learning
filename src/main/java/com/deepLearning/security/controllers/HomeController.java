package com.deepLearning.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/home")
public class HomeController {


    @GetMapping("/hello")
    public String get() {


        return "Hello World: " + LocalDateTime.now().toString();
    }

}
