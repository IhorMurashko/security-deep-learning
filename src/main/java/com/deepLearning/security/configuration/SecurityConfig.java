package com.deepLearning.security.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        authorize ->
                                authorize.requestMatchers("/home/hello").permitAll()
                                        .requestMatchers("/secured/**").hasRole("ADMIN"))
                .formLogin(Customizer.withDefaults())
                .build();


    }


//    @Bean
//    public UserDetailsManager inMemoryUserDetailsManager() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .authorities("admin")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(user);
//    }


}
