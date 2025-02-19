package com.deepLearning.security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * HomeController is a simple REST controller used to demonstrate role-based security in a Spring Security application.
 * <p>
 * The controller exposes two endpoints:
 * <ul>
 *   <li><b>/home/user</b>: Accessible only by users with the "USER" role.</li>
 *   <li><b>/home/admin</b>: Accessible only by users with the "ADMIN" role.</li>
 * </ul>
 * <p>
 * Each endpoint returns a simple greeting message along with the current date and time.
 * This controller is intended for testing purposes and can be extended or modified for more complex scenarios.
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    /**
     * Endpoint accessible by users with the "USER" role.
     * <p>
     * Returns a greeting message along with the current date and time.
     *
     * @return a String message containing "Hello World" and the current timestamp.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String getUser() {
        return "Hello World: " + LocalDateTime.now().toString();
    }

    /**
     * Endpoint accessible by users with the "ADMIN" role.
     * <p>
     * Returns a greeting message along with the current date and time.
     *
     * @return a String message containing "Hello World" and the current timestamp.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin() {
        return "Hello World: " + LocalDateTime.now().toString();
    }
}