package com.deepLearning.security.jwt;

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
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;


    /**
     * Calls the parent class {@code handle()} method to forward or redirect to the target
     * URL, and then calls {@code clearAuthenticationAttributes()} to remove any leftover
     * session data.
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String email = oAuth2User.getAttribute("email");

        User user = null;

        if (userService.isExistUsername(email)) {
            user = (User) userDetailsService.loadUserByUsername(email);
        } else {
            user = userService.save(new User(
                    email,
                    null,
                    oAuth2User.getAttribute("picture"),
                    new ArrayList<>() {{
                        add(Roles.ROLE_USER.name());
                    }}
            ));
        }


        final String token = jwtTokenProvider.generateToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/home/user")
                .queryParam("token", token)
                .build().toUriString();
        System.out.println(token);
        System.out.println(user);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);



    }
}