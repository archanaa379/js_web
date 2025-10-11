package com.example.bank.web;

import com.example.bank.domain.User;
import com.example.bank.service.AccountService;
import com.example.bank.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Validated
public class AuthController {
    private final UserService userService;
    private final AccountService accountService;

    public AuthController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        return session.getAttribute("uid") == null ? "login" : "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/login")
    public String doLogin(HttpSession session,
                          @RequestParam @Email String email,
                          @RequestParam String password,
                          Model model) {
        User user = userService.authenticate(email, password);
        if (user == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
        session.setAttribute("uid", user.getId());
        // Ensure user has at least one account
        accountService.getAccountsForUser(user.getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/register")
    public String doRegister(HttpSession session,
                             @RequestParam @Email String email,
                             @RequestParam @NotBlank String fullName,
                             @RequestParam String password) {
        User user = userService.registerUser(email, fullName, password);
        accountService.getAccountsForUser(user.getId());
        session.setAttribute("uid", user.getId());
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
