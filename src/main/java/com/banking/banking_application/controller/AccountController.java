package com.banking.banking_application.controller;

import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.AccountType;
import com.banking.banking_application.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String number, @RequestParam AccountType type,
            @RequestParam Double initial, Principal principal) {
        Account created = accountService.createAccount(number, type, initial, principal.getName());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Account>> myAccounts(Principal principal) {
        return ResponseEntity.ok(accountService.getAccountsForUser(principal.getName()));
    }
}