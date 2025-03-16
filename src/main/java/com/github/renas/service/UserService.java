package com.github.renas.service;

import com.github.renas.persistance.UserRepository;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDao registerUser(String username, String password, String email, Role role) {
        String encodedPassword = passwordEncoder.encode(password);
        UserDao user = new UserDao(UUID.randomUUID(),username, encodedPassword, email, role);
        return userRepository.save(user);
    }
}
