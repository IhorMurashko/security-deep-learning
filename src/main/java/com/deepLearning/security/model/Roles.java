package com.deepLearning.security.model;


/**
 * Roles is an enumeration of the security roles available in the application.
 * <p>
 * This enum defines the two primary roles used for access control:
 * <ul>
 *   <li><b>ROLE_USER</b>: Represents a standard user with limited privileges.</li>
 *   <li><b>ROLE_ADMIN</b>: Represents an administrator with elevated privileges.</li>
 * </ul>
 * <p>
 * These roles can be used with Spring Security's method security annotations (e.g., {@code @PreAuthorize})
 * to restrict access to specific endpoints or functionalities based on the user's assigned role.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 *   // Checking if a user has the ROLE_ADMIN role
 *   if(user.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.name()))) {
 *       // grant access to admin features
 *   }
 * </pre>
 */
public enum Roles {
    ROLE_USER,
    ROLE_ADMIN;
}