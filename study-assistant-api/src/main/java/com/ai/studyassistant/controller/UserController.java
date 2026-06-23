package com.ai.studyassistant.controller;


import com.ai.studyassistant.dto.UserResponse;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.UserRepository;
import com.ai.studyassistant.security.JwtUtil;
import com.ai.studyassistant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(
            UserService userService,
            UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String username = JwtUtil.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow();

        return ResponseEntity.ok(
                new UserResponse(
                        user.getId(),
                        user.getUsername()
                )
        );
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user
    ) {
        return ResponseEntity.ok(
                userService.createUser(
                        user.getUsername(),
                        user.getPassword()
                )
        );
    }
}
