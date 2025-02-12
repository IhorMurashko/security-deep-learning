package com.deepLearning.security.securityServices;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.exceptions.UserAlreadyExist;
import com.deepLearning.security.jwt.JwtTokenProvider;
import com.deepLearning.security.model.Roles;
import com.deepLearning.security.model.User;
import com.deepLearning.security.userServices.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String authenticate(@NonNull AuthCredentials credentials) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(credentials.username());

        return jwtTokenProvider.generateToken(userDetails);
    }

    @Override
    public boolean registration(@NonNull AuthCredentials credentials) {

        boolean isExistUsername = userService.isExistUsername(credentials.username());

        if (isExistUsername) {
            throw new UserAlreadyExist(
                    String.format("user with username %s already exist", credentials.username()));

        } else {
             userService.save(new User(
                    credentials.username(),
                    passwordEncoder.encode(credentials.password()),
                    new ArrayList<String>() {{
                        add(Roles.ROLE_USER.name());
                    }}
            ));
             return true;
        }


    }
}
