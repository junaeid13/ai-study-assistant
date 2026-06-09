package com.ai.studyassistant.controller;

import com.ai.studyassistant.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        // Simple hardcoded auth (replace later with DB)
        if ("admin".equals(username) && "admin123".equals(password)) {

            String token = JwtUtil.generateToken(username);

            return ResponseEntity.ok(Map.of(
                    "token", token
            ));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}