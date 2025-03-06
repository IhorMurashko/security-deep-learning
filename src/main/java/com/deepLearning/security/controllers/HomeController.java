package com.deepLearning.security.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 */
@RestController
@RequestMapping("/home")
@Tag(name = "Home Controller", description = "Endpoints for role-based access control demonstration")
public class HomeController {

    /**
     * Endpoint accessible by users with the "USER" role.
     * Returns a greeting message along with the current date and time.
     *
     * @return a String message containing "Hello World" and the current timestamp.
     */
    @Operation(summary = "User Access", description = "Accessible only for users with the 'USER' role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully accessed user endpoint"),
            @ApiResponse(responseCode = "403", description = "Access denied - user does not have the required role")
    })
    @SecurityRequirement(name = "JWT")

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String getUser() {
        return "Hello World: " + LocalDateTime.now();
    }

    /**
     * Endpoint accessible by users with the "ADMIN" role.
     * Returns a greeting message along with the current date and time.
     *
     * @return a String message containing "Hello admin" and the current timestamp.
     */
    @Operation(summary = "Admin Access", description = "Accessible only for users with the 'ADMIN' role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully accessed admin endpoint"),
            @ApiResponse(responseCode = "403", description = "Access denied - user does not have the required role")
    })
    @SecurityRequirement(name = "JWT")

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin() {
        return "Hello admin: " + LocalDateTime.now();
    }

    @GetMapping("/free")
    @PreAuthorize("permitAll()")
    public String getFree() {
        return "Hello free: " + LocalDateTime.now();
    }

}
