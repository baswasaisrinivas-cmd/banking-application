package com.banking.banking_application.controller;

import com.banking.banking_application.model.Transaction;
import com.banking.banking_application.model.dto.TransactionDTO;
import com.banking.banking_application.service.AccountService;
import com.banking.banking_application.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionDTO dto, Principal principal) {
        Transaction tx = accountService.deposit(dto.getToAccountNumber(), dto.getAmount(), dto.getDescription());
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionDTO dto, Principal principal) {
        Transaction tx = accountService.withdraw(dto.getFromAccountNumber(), dto.getAmount(), dto.getDescription());
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDTO dto, Principal principal) {
        Transaction tx = accountService.transfer(dto.getFromAccountNumber(), dto.getToAccountNumber(), dto.getAmount(),
                dto.getDescription());
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Page<Transaction>> history(@PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Transaction> p = transactionService.findByAccountNumber(accountNumber, PageRequest.of(page, size));
        return ResponseEntity.ok(p);
    }
}