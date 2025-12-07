package com.banking.banking_application.controller;

import com.banking.banking_application.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("totalBalance", bankService.getTotalBankBalance());
        m.put("totalCustomers", bankService.getTotalCustomers());
        m.put("totalAccounts", bankService.getTotalAccounts());
        return ResponseEntity.ok(m);
    }
}
