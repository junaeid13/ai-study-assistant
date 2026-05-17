package com.ai.studyassistant.service;

import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(
            String username,
            String password
    ) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
