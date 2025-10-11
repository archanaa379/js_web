package com.example.bank.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
