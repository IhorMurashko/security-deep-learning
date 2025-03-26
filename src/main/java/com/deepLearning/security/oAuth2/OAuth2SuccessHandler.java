package com.deepLearning.security.oAuth2;

import com.deepLearning.security.jwt.JwtTokenProvider;
import com.deepLearning.security.model.Roles;
import com.deepLearning.security.model.User;
import com.deepLearning.security.userServices.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Set;

/**
 * OAuth2SuccessHandler handles successful OAuth2 authentication events.
 * <p>
 * Upon a successful OAuth2 login, this handler performs the following steps:
 * <ol>
 *   <li>Extracts the authenticated user's details from the OAuth2User principal.</li>
 *   <li>Retrieves the user's email from the OAuth2 attributes.</li>
 *   <li>Checks if a user with the given email already exists:
 *       <ul>
 *         <li>If the user exists, loads the user details via UserDetailsService.</li>
 *         <li>If the user does not exist, creates a new user with the given email, profile picture,
 *         and assigns the default role (ROLE_USER).</li>
 *       </ul>
 *   </li>
 *   <li>Generates a new JWT access token for the authenticated user using JwtTokenProvider.</li>
 *   <li>Builds a target URL with the access token as a query parameter and redirects the user to that URL.</li>
 * </ol>
 * <p>
 * This implementation assumes that the OAuth2 provider returns an "email" and "picture" attribute.
 * <p>
 * <b>Note:</b> Instead of using System.out.println for logging, it is recommended to use a logging framework.
 * Additionally, consider using immutable collections for the roles assignment.
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    /**
     * Handles a successful OAuth2 authentication by processing the authenticated user's information,
     * generating a JWT access token, and redirecting the user to a predefined URL with the token attached as a parameter.
     *
     * @param request        the HttpServletRequest object.
     * @param response       the HttpServletResponse object.
     * @param authentication the Authentication object containing the authenticated principal.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet-specific error occurs.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        final String email = oAuth2User.getAttribute("email");

        User user;
        if (userService.isExistUsername(email)) {
            user = (User) userDetailsService.loadUserByUsername(email);
        } else {

            // Create new user with default role ROLE_USER using an immutable list
            user = userService.save(new User(
                    email,
                    null,
                    oAuth2User.getAttribute("picture"),
                    Set.of(Roles.ROLE_USER)
            ));
        }

        final String token = jwtTokenProvider.generateAccessToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/home/user")
                .queryParam("token", token)
                .build().toUriString();


        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}