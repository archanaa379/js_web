package com.example.bank.web;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.User;
import com.example.bank.service.AccountService;
import com.example.bank.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final AccountService accountService;

    public DashboardController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) return "redirect:/login";
        User user = userService.getById(uid);
        List<Account> accounts = accountService.getAccountsForUser(uid);
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(HttpSession session,
                          @RequestParam Long accountId,
                          @RequestParam BigDecimal amount,
                          @RequestParam(required = false) String description) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) return "redirect:/login";
        accountService.deposit(uid, accountId, amount, description);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpSession session,
                           @RequestParam Long accountId,
                           @RequestParam BigDecimal amount,
                           @RequestParam(required = false) String description) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) return "redirect:/login";
        accountService.withdraw(uid, accountId, amount, description);
        return "redirect:/dashboard";
    }
}
