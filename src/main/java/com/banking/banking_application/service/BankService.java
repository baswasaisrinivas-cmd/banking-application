package com.banking.banking_application.service;

import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BankService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AuthService authService;
    private final com.banking.banking_application.repository.AccountRepository accountRepository;

    public BankService(AccountService accountService, TransactionService transactionService, AuthService authService,
            com.banking.banking_application.repository.AccountRepository accountRepository) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String number, com.banking.banking_application.model.AccountType type, Double initial,
            String username) {
        return accountService.createAccount(number, type, initial, username);
    }

    public List<Account> getAccounts(String username) {
        return accountService.getAccountsForUser(username);
    }

    public Transaction deposit(String accountNumber, Double amount, String description) {
        return accountService.deposit(accountNumber, amount, description);
    }

    // New aggregate helpers used by BankController
    public Double getTotalBankBalance() {
        return accountRepository.findAll().stream().mapToDouble(a -> a.getBalance() == null ? 0.0 : a.getBalance())
                .sum();
    }

    public long getTotalAccounts() {
        return accountRepository.count();
    }

    public long getTotalCustomers() {
        Set<Long> owners = accountRepository.findAll().stream()
                .map(a -> a.getOwner() != null ? a.getOwner().getId() : null)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        return owners.size();
    }
}