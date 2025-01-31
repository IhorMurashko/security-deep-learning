package com.deepLearning.security.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "security_users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String username;
    private String password;

    @JoinColumn(name = "username")
    @OneToMany(fetch = FetchType.EAGER)
    private List<AuthorityEntity> authorities = new ArrayList<>();


}
