package com.deepLearning.security.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class HomeControllerTest {


    @Autowired
    private MockMvc mockMvc;


    private final String userUlr = "/home/user";
    private final String freeUrl = "/home/free";
    private final String adminUrl = "/home/admin";

    @Test
    @WithAnonymousUser
    @DisplayName("GET /home/user – is forbidden for anonymous user")
    void userEndpoint_whenUserIsAnonymous_thenStatusForbidden() throws Exception {
        mockMvc.perform(get(userUlr))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /home/user – has access for user")
    void userEndpoint_whenUserHasRoleUser_thenOk() throws Exception {
        mockMvc.perform(get(userUlr))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /home/user – has access for user")
    void userEndpoint_whenUserHasRoleAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get(userUlr))
                .andExpect(status().isForbidden());
    }



}