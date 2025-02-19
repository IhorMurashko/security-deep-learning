package com.deepLearning.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * JwtAuthEntryPoint is a custom implementation of the AuthenticationEntryPoint interface.
 * <p>
 * This component is invoked whenever an unauthenticated user attempts to access a secured REST endpoint.
 * It is responsible for sending an HTTP 401 Unauthorized error response to the client.
 * <p>
 * The handler logs the authentication exception details and then sends an error response.
 * This is useful for diagnosing authentication issues and ensuring that unauthorized access is properly denied.
 * <p>
 * Example usage:
 * <pre>
 *   http.exceptionHandling()
 *       .authenticationEntryPoint(new JwtAuthEntryPoint());
 * </pre>
 */
@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     * <p>
     * This method is called when an exception is thrown due to an unauthenticated user attempting to access a secured resource.
     * It logs the error and sends a 401 Unauthorized response to the client.
     *
     * @param request       the HttpServletRequest in which the exception occurred.
     * @param response      the HttpServletResponse to which the error response will be sent.
     * @param authException the exception that caused the invocation.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet-specific error occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error(authException.getMessage(), authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
