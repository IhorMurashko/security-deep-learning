package com.deepLearning.security.configuration;

import com.deepLearning.security.jwt.JwtAccessDeniedHandler;
import com.deepLearning.security.jwt.JwtAuthEntryPoint;
import com.deepLearning.security.jwt.JwtAuthFilter;
import com.deepLearning.security.oAuth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * SecurityConfig is the central configuration class for Spring Security in this RESTful application.
 * <p>
 * This configuration class performs the following functions:
 * <ul>
 *   <li><b>Disables CSRF protection</b> and configures CORS to allow cross-origin requests from any origin,
 *       with support for all common HTTP methods and headers, including credentials.</li>
 *   <li><b>Sets the session management policy</b> to STATELESS, ensuring that the application does not
 *       maintain any server-side session state, in line with RESTful best practices.</li>
 *   <li><b>Configures exception handling</b> to use custom entry point and access denied handlers (JwtAuthEntryPoint and JwtAccessDeniedHandler),
 *       which provide appropriate HTTP error responses when authentication or authorization fails.</li>
 *   <li><b>Disables form-based login and HTTP Basic authentication</b> as the application leverages JWT and OAuth2 for authentication.</li>
 *   <li><b>Defines URL access rules</b> via authorizeHttpRequests:
 *       <ul>
 *         <li>Endpoints under "/api/auth/**", "/error", and "/oauth2/**" are publicly accessible.</li>
 *         <li>Endpoints under "/home/**" require authentication.</li>
 *         <li>The logout endpoint "/api/log/logout" is publicly accessible.</li>
 *         <li>All other endpoints are permitted by default.</li>
 *       </ul>
 *       Review these rules to ensure they meet your security requirements.</li>
 *   <li><b>Configures OAuth2 login</b> with custom endpoints:
 *       <ul>
 *         <li>The authorization endpoint is set to "/oauth2/login".</li>
 *         <li>The redirection (callback) endpoint is set to "/login/oauth2/code/*".</li>
 *         <li>A custom OAuth2SuccessHandler is specified to process successful OAuth2 authentication events.</li>
 *       </ul>
 *   </li>
 *   <li><b>Registers a DaoAuthenticationProvider</b> that uses the provided UserDetailsService and a BCryptPasswordEncoder,
 *       supporting authentication based on username and password.</li>
 *   <li><b>Defines a custom JwtAuthFilter</b> that is added to the filter chain before the
 *       UsernamePasswordAuthenticationFilter, ensuring that JWT authentication is processed early in the request flow.</li>
 *   <li><b>Exposes an AuthenticationManager bean</b> for performing authentication operations in the application.</li>
 * </ul>
 * <p>
 * This configuration is designed according to best practices for stateless RESTful security and integrates JWT and OAuth2-based authentication.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Custom JWT authentication filter that validates tokens in incoming requests.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Service used for loading user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Entry point for handling authentication errors (e.g., invalid or missing JWT).
     */
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    /**
     * Handler for access denied exceptions.
     */
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * Custom OAuth2 success handler that processes successful OAuth2 authentication events.
     */
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    /**
     * Configures the main SecurityFilterChain for the application.
     * <p>
     * This method configures:
     * <ul>
     *   <li>CSRF (disabled) and CORS settings.</li>
     *   <li>Stateless session management.</li>
     *   <li>Exception handling for authentication and authorization failures.</li>
     *   <li>Disabling of form login and HTTP Basic authentication.</li>
     *   <li>URL access rules for different endpoints.</li>
     *   <li>OAuth2 login with custom endpoints and success handling.</li>
     *   <li>The registration of the DaoAuthenticationProvider and addition of the JWT filter into the chain.</li>
     * </ul>
     *
     * @param http the HttpSecurity object to be configured.
     * @return the constructed SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection since we are using JWT and stateless sessions
                .csrf(AbstractHttpConfigurer::disable)
                // Configure CORS settings
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure exception handling with custom entry point and access denied handler
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                // Disable form login and HTTP Basic authentication
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Define URL authorization rules
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers("/api/auth/**", "/error", "/oauth2/**").permitAll()
                            .requestMatchers("/home/user", "/home/admin").authenticated()
                            .requestMatchers("/api/log/logout").authenticated()
                            .requestMatchers("/h2-console/**").permitAll()
                            .anyRequest().permitAll();
                })
//                .headers(headers->headers.frameOptions(
//                        HeadersConfigurer.FrameOptionsConfig::disable
//                ))
                // Configure OAuth2 login with custom endpoints and success handler
                .oauth2Login(oAuth2 ->
                        oAuth2.authorizationEndpoint(authorization ->
                                        authorization.baseUri("/oauth2/login"))
                                .redirectionEndpoint(redirection ->
                                        redirection.baseUri("/login/oauth2/code/*"))
                                .successHandler(oAuth2SuccessHandler)
                )
                // Register the DaoAuthenticationProvider
                .authenticationProvider(authenticationProvider())
                // Add the custom JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean that uses BCrypt hashing.
     * <p>
     * BCryptPasswordEncoder is a strong hashing function suitable for storing user passwords.
     *
     * @return an instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and returns a DaoAuthenticationProvider.
     * <p>
     * This provider uses the injected UserDetailsService and BCryptPasswordEncoder for authenticating users by their credentials.
     *
     * @return a configured DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Exposes an AuthenticationManager bean for performing authentication operations.
     * <p>
     * The AuthenticationManager is built using the shared AuthenticationManagerBuilder,
     * and registers the DaoAuthenticationProvider configured in this class.
     *
     * @param http the HttpSecurity instance.
     * @return the configured AuthenticationManager.
     * @throws Exception if an error occurs during the building of the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }
}
