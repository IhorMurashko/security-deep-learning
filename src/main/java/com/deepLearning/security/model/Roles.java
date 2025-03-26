package com.deepLearning.security.model;


import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.core.GrantedAuthority;

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
public enum Roles implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    /**
     * If the <code>GrantedAuthority</code> can be represented as a <code>String</code>
     * and that <code>String</code> is sufficient in precision to be relied upon for an
     * access control decision by an {@link AccessDecisionManager} (or delegate), this
     * method should return such a <code>String</code>.
     * <p>
     * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision
     * as a <code>String</code>, <code>null</code> should be returned. Returning
     * <code>null</code> will require an <code>AccessDecisionManager</code> (or delegate)
     * to specifically support the <code>GrantedAuthority</code> implementation, so
     * returning <code>null</code> should be avoided unless actually required.
     *
     * @return a representation of the granted authority (or <code>null</code> if the
     * granted authority cannot be expressed as a <code>String</code> with sufficient
     * precision).
     */
    @Override
    public String getAuthority() {
        return name();
    }
}