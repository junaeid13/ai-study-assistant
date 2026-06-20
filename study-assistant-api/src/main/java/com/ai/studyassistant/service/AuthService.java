package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.LoginRequest;
import com.ai.studyassistant.dto.RegisterRequest;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.UserRepository;
import com.ai.studyassistant.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException(
                    "Username already exists"
            );
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        return JwtUtil.generateToken(
                user.getUsername()
        );

    }
}
