package com.example.bank.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.Account;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);
}
