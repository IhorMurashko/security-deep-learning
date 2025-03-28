package com.deepLearning.security.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an application user and implements Spring Security's UserDetails interface.
 * <p>
 * This entity is mapped to the "security_users" table in the database and contains the following fields:
 * <ul>
 *   <li><b>id</b> - The unique identifier of the user, generated automatically.</li>
 *   <li><b>username</b> - The unique username of the user.</li>
 *   <li><b>password</b> - The user's password (stored in encoded form in production).</li>
 *   <li><b>image</b> - A URL or path to the user's profile image.</li>
 *   <li><b>roles</b> - A collection of roles assigned to the user (e.g., "ROLE_USER", "ROLE_ADMIN").</li>
 * </ul>
 * <p>
 * The class implements the {@code UserDetails} interface by providing:
 * <ul>
 *   <li>Authorities based on the user's roles, mapped to {@code SimpleGrantedAuthority} instances.</li>
 *   <li>Status methods that indicate that the account is non-expired, non-locked, credentials non-expired, and enabled.</li>
 * </ul>
 * <p>
 * <b>Note:</b> In this implementation, the toString() method generated by Lombok includes all fields.
 * For security reasons, consider excluding sensitive fields such as the password (e.g., using
 * {@code @ToString(exclude = "password")}).
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * User user = new User("john.doe", "securePassword", "/images/john.png", List.of("ROLE_USER"));
 * </pre>
 *
 * @see org.springframework.security.core.userdetails.UserDetails
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * The unique identifier of the user. It is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user. It must be unique.
     */
    @Column(unique = true)
    private String username;

    /**
     * The password of the user. In production, this should be stored in an encoded form.
     */
    private String password;

    /**
     * The URL or path to the user's profile image.
     */
    private String image;
    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;

    /**
     * The list of roles assigned to the user (e.g., "ROLE_USER", "ROLE_ADMIN").
     * This field is eagerly fetched from the database.
     */
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Roles.class)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles = new HashSet<>();

    /**
     * Constructs a new User with the specified username, password, image, and roles.
     *
     * @param username the username of the user.
     * @param password the user's password.
     * @param image    the URL or path to the user's profile image.
     * @param roles    a list of roles assigned to the user.
     */
    public User(String username, String password, String image, Set<Roles> roles) {
        this.username = username;
        this.password = password;
        this.image = image;
        this.roles = roles;
    }

    /**
     * Returns the authorities granted to the user.
     * <p>
     * The roles are mapped to {@link SimpleGrantedAuthority} instances.
     *
     * @return a collection of granted authorities, never {@code null}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} if the account is non-expired; {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return {@code true} if the account is not locked; {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return {@code true} if the credentials are non-expired; {@code false} otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return {@code true} if the user is enabled; {@code false} otherwise.
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}