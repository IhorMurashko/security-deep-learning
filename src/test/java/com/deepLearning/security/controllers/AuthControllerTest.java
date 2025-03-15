package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.dto.TokensDto;
import com.deepLearning.security.securityServices.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthCredentials authCredentials;

    @Captor
    ArgumentCaptor<AuthCredentials> credentialsCaptor;

    private final String singUpUrl = "/api/auth/sign-up";
    private final String singInUrl = "/api/auth/sign-in";
    private final String refresh_token = "/api/auth/refresh-token";

    @BeforeEach
    void setUp() {
        this.authCredentials = new AuthCredentials("user", "pass");
    }

    @Test
    @WithAnonymousUser
    @DisplayName("registration_statusCREATED")
    void getStatusCREATED_whenUserAnonymousAndUserNotExist() throws Exception {
        doReturn(true).when(authService).registration(authCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post(singUpUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCredentials)))
                .andExpect(status().isCreated());

        verify(authService).registration(credentialsCaptor.capture());

        verify(authService, times(1)).registration(authCredentials);
        verifyNoMoreInteractions(authService);

        assertEquals(credentialsCaptor.getValue(), authCredentials);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("registration_statusBAD_REQUEST_anonymousRequest_butUserHasAlreadyExist")
    void getStatusBadRequest_whenUserRequestIsAnonymous_butUserHasAlreadyExist() throws Exception {
        doReturn(false).when(authService).registration(authCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post(singUpUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCredentials)))
                .andExpect(status().isBadRequest());

        verify(authService).registration(credentialsCaptor.capture());

        verify(authService, times(1)).registration(authCredentials);
        verifyNoMoreInteractions(authService);

        assertEquals(credentialsCaptor.getValue(), authCredentials);
    }


    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    @DisplayName("accessDeniedStatus_whenUserHasAlreadyAuthenticated")
    void getAccessDenied_whenUserHasAlreadyAuthenticated() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(singUpUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCredentials)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("signIn_successful_returnsTokens")
    @WithAnonymousUser
    void getTokensAfterValidCredentials() throws Exception {

        TokensDto expectedTokens = new TokensDto("accessToken", "refreshToken");

        when(authService.authenticate(authCredentials)).thenReturn(expectedTokens);

        mockMvc.perform(MockMvcRequestBuilders.post(singInUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCredentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedTokens.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(expectedTokens.refreshToken()));

        verify(authService).authenticate(credentialsCaptor.capture());
        assertEquals(credentialsCaptor.getValue(), authCredentials);

        verify(authService, times(1)).authenticate(authCredentials);
        verifyNoMoreInteractions(authService);
    }



}
