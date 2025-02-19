package com.deepLearning.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAccessDeniedHandler is a custom implementation of the AccessDeniedHandler interface.
 * <p>
 * This handler is invoked when an authenticated user attempts to access a resource for which they do not have the required permissions.
 * In such cases, it sends an HTTP 403 Forbidden response to the client.
 * <p>
 * This is typically used in conjunction with JWT-based authentication, where access control is enforced by checking user roles and authorities.
 * <p>
 * Example usage:
 * <pre>
 *   http.exceptionHandling()
 *       .accessDeniedHandler(new JwtAccessDeniedHandler());
 * </pre>
 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles an access denied failure.
     * <p>
     * This method sends a 403 Forbidden response with a simple "Forbidden" message.
     *
     * @param request               the HttpServletRequest that resulted in an AccessDeniedException
     * @param response              the HttpServletResponse to send the error response
     * @param accessDeniedException the exception that was thrown due to insufficient permissions
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.error(accessDeniedException.getMessage(), accessDeniedException);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}