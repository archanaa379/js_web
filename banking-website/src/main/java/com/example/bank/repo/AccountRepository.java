package com.example.bank.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.domain.Account;
import com.example.bank.domain.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
}
