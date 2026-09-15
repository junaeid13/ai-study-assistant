package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.auth.AuthResponse;
import com.ai.studyassistant.dto.auth.LoginRequest;
import com.ai.studyassistant.dto.auth.RegisterRequest;
import com.ai.studyassistant.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return ResponseEntity.ok("User registered successflly");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        String token = authService.login(request);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}