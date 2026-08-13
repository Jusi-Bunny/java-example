package com.example.satoken.controller;

import com.example.satoken.mapper.SysPermissionMapper;
import com.example.satoken.mapper.SysRoleMapper;
import com.example.satoken.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jusi
 * @date 2026/8/13
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationAuthorizationMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private SysRoleMapper sysRoleMapper;

    @MockitoBean
    private SysPermissionMapper sysPermissionMapper;

    @Test
    void currentUser_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }
}
