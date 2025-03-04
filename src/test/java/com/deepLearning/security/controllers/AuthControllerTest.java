package com.deepLearning.security.controllers;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.securityServices.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.*;
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

    private AuthCredentials credentials;

    @BeforeEach
    void setUp() {
        this.credentials = new AuthCredentials("user", "pass");
    }

    @Test
    @WithAnonymousUser
    @DisplayName("registration_CREATED")
    void getStatusCREATED_whenUserAnonymousAndUserNotExist() throws Exception {
        doReturn(true).when(authService).registration(credentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).registration(credentials);
        verifyNoMoreInteractions(authService);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("registration_BAD_REQUEST")
    void getStatusBadRequest_whenUserAnonymousAndUserIsExist() throws Exception {
        doReturn(false).when(authService).registration(credentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).registration(credentials);
        verifyNoMoreInteractions(authService);
    }


    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    @DisplayName("accessDenied_whenUserIsAuthentication")
    void getAccessDenied_whenUserIsAuthentication() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(authService);
    }
}
