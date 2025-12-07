package com.banking.banking_application.controller;

import com.banking.banking_application.model.User;
import com.banking.banking_application.service.AuthService;
import com.banking.banking_application.model.dto.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // JSON API registration
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerJson(@RequestBody LoginDTO dto) {
        try {
            User created = authService.register(dto.getUsername(), dto.getUsername() + "@bank.com", dto.getPassword());
            created.setPassword(null);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            logger.warn("Registration failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Form-based registration (from register.html)
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerForm(LoginDTO dto) {
        try {
            authService.register(dto.getUsername(), dto.getUsername() + "@bank.com", dto.getPassword());
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException ex) {
            logger.warn("Registration failed (form): {}", ex.getMessage());
            return "redirect:/register?error=true";
        }
    }
}