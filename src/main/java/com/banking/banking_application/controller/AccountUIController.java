package com.banking.banking_application.controller;

import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.AccountType;
import com.banking.banking_application.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountUIController {
    private final AccountService accountService;

    public AccountUIController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/list")
    public String listAccounts(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "admin";
        List<Account> accounts = accountService.getAccountsForUser(username);
        model.addAttribute("accounts", accounts);
        return "accounts/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/create";
    }

    @PostMapping("/create")
    public String createAccount(@RequestParam String accountNumber, 
                                @RequestParam AccountType accountType,
                                @RequestParam Double balance,
                                Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "admin";
            accountService.createAccount(accountNumber, accountType, balance, username);
            return "redirect:/accounts/list?success=true";
        } catch (IllegalArgumentException ex) {
            return "redirect:/accounts/create?error=" + ex.getMessage();
        }
    }

    @GetMapping("/view")
    public String viewAccount(@RequestParam Long id, Model model) {
        Account account = accountService.getAccountById(id);
        model.addAttribute("account", account);
        return "accounts/view";
    }
}
