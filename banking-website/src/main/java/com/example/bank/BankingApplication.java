package com.example.bank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import com.example.bank.service.AccountService;
import com.example.bank.service.UserService;
import com.example.bank.domain.User;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDemoData(UserService userService, AccountService accountService) {
        return args -> {
            if (userService.countUsers() == 0) {
                User demo = userService.registerUser("demo@bank.local", "Demo User", "demo123");
                accountService.deposit(demo.getId(),
                        accountService.getAccountsForUser(demo.getId()).get(0).getId(),
                        new BigDecimal("1000.00"),
                        "Initial deposit");
            }
        };
    }
}
