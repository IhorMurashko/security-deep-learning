package com.deepLearning.security.jwt;

import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.model.Roles;
import com.deepLearning.security.model.User;
import com.deepLearning.security.userServices.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("dev")
class JwtTokenManagerTest {


    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtTokenManager jwtTokenManager;

    @Captor
    private ArgumentCaptor<String> tokenCaptor;

    private final String newAccessToken = "new-access-token";
    private final String newRefreshToken = "new-refresh-token";


    private final TokensDto tokensDto =
            new TokensDto("old-access-token", "old-refresh-token");

    private User user;
    private CustomUserDetails customUserDetails;
    private final String invalidRefreshTokenExceptionMessage = "Refresh token is invalid";

    @BeforeEach
    void setUp() {
        this.user = new User("user", "password", null, Collections.singleton(Roles.ROLE_USER));

        customUserDetails = new CustomUserDetails(user);
    }


    @Test
    void getNewAccessTokenAndOldRefreshToken_WhenRefreshTokenIsValidAndAccessTokenIsExpired() {

        doReturn(true).when(jwtTokenProvider)
                .validateToken(tokensDto.refreshToken());

        doReturn(user.getUsername()).when(jwtTokenProvider)
                .getUsernameFromToken(tokensDto.refreshToken());

        doReturn(customUserDetails).when(userDetailsService)
                .loadUserByUsername(user.getUsername());

        doReturn(newAccessToken).when(jwtTokenProvider)
                .generateAccessToken(customUserDetails);

        doReturn(false).when(jwtTokenProvider)
                .isRefreshTokenExpiredSoon(tokensDto.refreshToken());

        TokensDto result = jwtTokenManager.manageTokens(tokensDto);

        verify(jwtTokenProvider).validateToken(tokenCaptor.capture());

        assertEquals(tokensDto.refreshToken(), tokenCaptor.getValue());

        assertNotNull(result);

        assertEquals(tokensDto.refreshToken(), result.refreshToken());
        assertEquals(newAccessToken, result.accessToken());

        verify(jwtTokenProvider, times(1)).validateToken(any());
        verify(jwtTokenProvider, times(1)).getUsernameFromToken(any());
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any());
        verify(jwtTokenProvider, times(1)).isRefreshTokenExpiredSoon(any());

        verifyNoMoreInteractions(jwtTokenProvider, userDetailsService);
    }


    @Test
    void getExceptionWhenRefreshTokenIsInvalid() {

        doThrow(new JwtException(invalidRefreshTokenExceptionMessage)).when(jwtTokenProvider)
                .validateToken(tokensDto.refreshToken());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jwtTokenManager.manageTokens(tokensDto));


        assertEquals(JwtException.class, exception.getClass());
        assertEquals(invalidRefreshTokenExceptionMessage, exception.getMessage());


        verify(jwtTokenProvider, times(1)).validateToken(any());
        verifyNoMoreInteractions(jwtTokenProvider, userDetailsService);
        verifyNoInteractions(userDetailsService);
    }


    @Test
    void getFalse_WhenRefreshTokenInvalid() {

        doReturn(false).when(jwtTokenProvider)
                .validateToken(tokensDto.refreshToken());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jwtTokenManager.manageTokens(tokensDto));

        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Invalid refresh token", exception.getMessage());


        verify(jwtTokenProvider, times(1)).validateToken(any());
        verifyNoMoreInteractions(jwtTokenProvider, userDetailsService);
        verifyNoInteractions(userDetailsService);
    }


    @Test
    void getNewAccessTokenAndNewRefreshToken_WhenRefreshTokenIsValidAndExpiredSoonAndAccessTokenIsExpired() {
        doReturn(true).when(jwtTokenProvider)
                .validateToken(tokensDto.refreshToken());

        doReturn(user.getUsername()).when(jwtTokenProvider)
                .getUsernameFromToken(tokensDto.refreshToken());

        doReturn(customUserDetails).when(userDetailsService)
                .loadUserByUsername(user.getUsername());

        doReturn(newAccessToken).when(jwtTokenProvider)
                .generateAccessToken(customUserDetails);

        doReturn(true).when(jwtTokenProvider)
                .isRefreshTokenExpiredSoon(tokensDto.refreshToken());

        doReturn(newRefreshToken).when(jwtTokenProvider)
                .generateRefreshToken(customUserDetails);


        TokensDto result = jwtTokenManager.manageTokens(tokensDto);

        assertNotNull(result);
        assertEquals(newRefreshToken, result.refreshToken());
        assertEquals(newAccessToken, result.accessToken());

        verify(jwtTokenProvider, times(1)).validateToken(any());
        verify(jwtTokenProvider, times(1)).getUsernameFromToken(any());
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any());
        verify(jwtTokenProvider, times(1)).isRefreshTokenExpiredSoon(any());
        verifyNoMoreInteractions(jwtTokenProvider, userDetailsService);

    }


}