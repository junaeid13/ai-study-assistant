package com.ai.studyassistant.config;

import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String @NonNull ... args) {

        if (userRepository.count() == 0) {

            User user = new User();

            user.setUsername("admin");
            user.setPassword("admin123");

            userRepository.save(user);

            System.out.println("✔ Default user created");
        } else {
            System.out.println("✔ Users already exist, skipping seed");
        }
    }
}