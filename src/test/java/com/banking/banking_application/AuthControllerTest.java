package com.banking.banking_application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterNewUserViaForm() throws Exception {
        // Test form-based registration with unique username
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "formuser_" + System.currentTimeMillis())
                .param("password", "testpass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered=true"));
    }

    @Test
    public void testRegisterDuplicateUserFailsViaForm() throws Exception {
        String uniqueUsername = "duplicate_" + System.currentTimeMillis();
        
        // Register first user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", uniqueUsername)
                .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered=true"));

        // Try to register same username again - should fail
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", uniqueUsername)
                .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error=true"));
    }

    @Test
    public void testRegisterViaJsonApi() throws Exception {
        // Test JSON-based registration
        String username = "jsonuser_" + System.currentTimeMillis();
        String jsonPayload = "{\"username\":\"" + username + "\",\"password\":\"jsonpass123\"}";
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").isEmpty());
    }

    @Test
    public void testGetLoginPageIsAccessible() throws Exception {
        // Login page should be accessible without authentication
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login")));
    }

    @Test
    public void testGetRegisterPageIsAccessible() throws Exception {
        // Register page should be accessible without authentication
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Register")));
    }

    @Test
    public void testRootPageIsAccessible() throws Exception {
        // Home page should be accessible
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
