package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.User;
import com.example.bank.repo.AccountRepository;
import com.example.bank.repo.TransactionRepository;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(UserRepository userRepository,
                          AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createDefaultAccountForUser(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 12));
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Account> accounts = accountRepository.findByUser(user);
        if (accounts.isEmpty()) {
            accounts.add(createDefaultAccountForUser(user));
        }
        return accounts;
    }

    @Transactional
    public Transaction deposit(Long userId, Long accountId, BigDecimal amount, String description) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (!account.getUser().getId().equals(userId)) throw new IllegalArgumentException("Unauthorized account access");
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setDescription(description == null ? "Deposit" : description);
        return transactionRepository.save(t);
    }

    @Transactional
    public Transaction withdraw(Long userId, Long accountId, BigDecimal amount, String description) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (!account.getUser().getId().equals(userId)) throw new IllegalArgumentException("Unauthorized account access");
        if (account.getBalance().compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount.negate());
        t.setDescription(description == null ? "Withdrawal" : description);
        return transactionRepository.save(t);
    }

    public List<Transaction> getTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return transactionRepository.findByAccountOrderByCreatedAtDesc(account);
    }
}
