package com.example.bank.service;

import com.example.bank.domain.User;
import com.example.bank.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(String email, String fullName, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
        user = userRepository.save(user);
        return user;
    }

    public User authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> BCrypt.checkpw(rawPassword, u.getPasswordHash()))
                .orElse(null);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
