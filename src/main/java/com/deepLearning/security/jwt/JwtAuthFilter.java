package com.deepLearning.security.jwt;

import com.deepLearning.security.redis.RevokedTokenServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JwtAuthFilter is a custom filter that intercepts HTTP requests to perform JWT-based authentication.
 * <p>
 * This filter performs the following steps:
 * <ol>
 *   <li>Extracts the JWT token from the "Authorization" header using the Bearer scheme.</li>
 *   <li>Validates the token using the JwtTokenProvider.</li>
 *   <li>Checks if the token is revoked via the RevokedTokenService.</li>
 *   <li>Ensures that the token is not a refresh token (as refresh tokens should not be used for authentication).</li>
 *   <li>Retrieves the username from the token and loads the corresponding UserDetails.</li>
 *   <li>If the user is found, sets the authentication in the SecurityContext, allowing the request to proceed as authenticated.</li>
 *   <li>If any validation fails, the filter sends a 401 Unauthorized error response.</li>
 * </ol>
 * <p>
 * This filter extends {@code OncePerRequestFilter} to guarantee that it is executed only once per request.
 * It is typically added to the security filter chain before the UsernamePasswordAuthenticationFilter.
 *
 * <p><b>Note:</b> The filter expects the JWT to be provided in the "Authorization" header in the format: "Bearer {token}".
 * If the token is missing, does not start with "Bearer ", or is invalid, the filter will log a warning or error and reject the request.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * JwtTokenProvider is responsible for token generation, validation, and extracting claims.
     */
    private final JwtTokenProvider tokenProvider;

    /**
     * UserDetailsService loads user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * RevokedTokenService is used to check if a token has been revoked (e.g., during logout).
     */
    private final RevokedTokenServiceImpl revokedTokenServiceImpl;

    /**
     * Filters each incoming HTTP request to perform JWT authentication.
     * <p>
     * The method extracts the JWT token from the request header, validates it, checks that it has not been revoked,
     * and ensures that it is an access token (not a refresh token). If validation is successful, it sets the authentication
     * in the SecurityContext. Otherwise, it sends a 401 Unauthorized error.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param filterChain the FilterChain to pass the request and response along the chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String token = extractToken(request);

            if (token != null && tokenProvider.validateToken(token)) {
                if (revokedTokenServiceImpl.isTokenRevoked(token)) {
                    throw new JwtException("Token has been revoked");
                }
                String tokenType = tokenProvider.extractClaimFromToken(token, claims ->
                        claims.get("token_type", String.class));
                if (tokenType != null && tokenType.equals("refreshToken")) {
                    throw new JwtException("Refresh token can't be used for authentication");
                }

                final String username = tokenProvider.getUsernameFromToken(token);
                UserDetails user = userDetailsService.loadUserByUsername(username);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("Set authentication in context holder for {}", user.getUsername());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            log.error("JwtAuthFilter: JwtException {}", e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     * <p>
     * The method expects the header to begin with the string "Bearer ".
     * If the header is absent or does not follow this format, a warning is logged and {@code null} is returned.
     *
     * @param request the HttpServletRequest from which the token is to be extracted
     * @return the JWT token string if present and well-formed; {@code null} otherwise
     */
    private String extractToken(HttpServletRequest request) {
        final String requestToken = request.getHeader("Authorization");

        if (requestToken == null) {
            log.warn("There is no Authorization header");
            return null;
        }
        if (!requestToken.startsWith("Bearer ")) {
            log.warn("Token does not begin with Bearer");
            return null;
        }
        return requestToken.substring(7);
    }
}