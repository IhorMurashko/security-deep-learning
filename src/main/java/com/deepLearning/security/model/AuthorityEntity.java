package com.deepLearning.security.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_authorities")
@Getter
@Setter
@NoArgsConstructor
public class AuthorityEntity {

    @Id
    private String username;

    private String authority;

}
