package com.banking.banking_application.service;

import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.Transaction;
import com.banking.banking_application.model.TransactionType;
import com.banking.banking_application.repository.AccountRepository;
import com.banking.banking_application.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable) {
        Page<Transaction> page = transactionRepository
                .findByFromAccount_AccountNumberOrToAccount_AccountNumber(accountNumber, accountNumber, pageable);
        logger.debug("Fetched transactions for account {} ({} items)", accountNumber, page.getTotalElements());
        return page;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction deposit(Long accountId, Double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        
        Transaction tx = new Transaction();
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setToAccount(account);
        tx.setDescription(description);
        tx.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount, String description) {
        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));
        
        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        accountRepository.save(from);
        accountRepository.save(to);
        
        Transaction tx = new Transaction();
        tx.setType(TransactionType.TRANSFER);
        tx.setAmount(amount);
        tx.setFromAccount(from);
        tx.setToAccount(to);
        tx.setDescription(description);
        tx.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(tx);
    }
}