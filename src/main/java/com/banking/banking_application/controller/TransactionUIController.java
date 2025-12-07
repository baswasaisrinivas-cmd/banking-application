package com.banking.banking_application.controller;

import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.Transaction;
import com.banking.banking_application.service.AccountService;
import com.banking.banking_application.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionUIController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionUIController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping("/history")
    public String transactionHistory(Model model, Principal principal) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactions/history";
    }

    @GetMapping("/deposit")
    public String depositForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "admin";
        List<Account> accounts = accountService.getAccountsForUser(username);
        model.addAttribute("accounts", accounts);
        return "transactions/deposit";
    }

    @PostMapping("/deposit")
    public String makeDeposit(@RequestParam Long accountId,
                              @RequestParam Double amount,
                              @RequestParam String description,
                              Principal principal) {
        try {
            transactionService.deposit(accountId, amount, description);
            return "redirect:/transactions/history?success=Deposit+successful";
        } catch (Exception ex) {
            return "redirect:/transactions/deposit?error=" + ex.getMessage();
        }
    }

    @GetMapping("/transfer")
    public String transferForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "admin";
        List<Account> accounts = accountService.getAccountsForUser(username);
        model.addAttribute("accounts", accounts);
        return "transactions/transfer";
    }

    @PostMapping("/transfer")
    public String makeTransfer(@RequestParam Long fromAccountId,
                               @RequestParam Long toAccountId,
                               @RequestParam Double amount,
                               @RequestParam String description,
                               Principal principal) {
        try {
            transactionService.transfer(fromAccountId, toAccountId, amount, description);
            return "redirect:/transactions/history?success=Transfer+successful";
        } catch (Exception ex) {
            return "redirect:/transactions/transfer?error=" + ex.getMessage();
        }
    }
}
