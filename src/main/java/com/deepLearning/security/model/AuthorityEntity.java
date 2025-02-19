package com.deepLearning.security.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * AuthorityEntity represents a security authority (or role) assigned to a user.
 * <p>
 * This JPA entity maps to the "security_authorities" table in the database.
 * It is used to store the relationship between a user (identified by the username)
 * and a specific authority (role or permission).
 * <p>
 * <b>Note:</b> In this simplified model, the username is used as the primary key,
 * which implies that each user can have only one authority. In a more comprehensive
 * security model where a user might have multiple authorities, consider using a composite
 * key (username and authority) or a separate primary key along with a many-to-many
 * relationship to represent multiple roles per user.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * AuthorityEntity authority = new AuthorityEntity();
 * authority.setUsername("john.doe");
 * authority.setAuthority("ROLE_USER");
 * </pre>
 */
@Entity
@Table(name = "security_authorities")
@Getter
@Setter
@NoArgsConstructor
public class AuthorityEntity {

    /**
     * The username serves as the primary key for the authority record.
     * In this model, it uniquely identifies a user for which a particular authority is assigned.
     */
    @Id
    private String username;

    /**
     * The authority (or role) assigned to the user.
     * This field represents the specific permission or role, such as "ROLE_USER" or "ROLE_ADMIN".
     */
    private String authority;
}
