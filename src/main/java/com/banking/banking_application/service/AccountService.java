package com.banking.banking_application.service;

import com.banking.banking_application.exception.AccountNotFoundException;
import com.banking.banking_application.exception.InsufficientBalanceException;
import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.AccountType;
import com.banking.banking_application.model.Transaction;
import com.banking.banking_application.model.TransactionType;
import com.banking.banking_application.model.User;
import com.banking.banking_application.repository.AccountRepository;
import com.banking.banking_application.repository.TransactionRepository;
import com.banking.banking_application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository, UserRepository userRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccount(String accountNumber, AccountType type, Double initialDeposit, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Business rule: minimums
        if (type == AccountType.SAVINGS && initialDeposit < 100.0) {
            throw new IllegalArgumentException("Minimum balance for SAVINGS is $100");
        }
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(type);
        account.setBalance(initialDeposit);
        account.setOwner(owner);
        account.setCreatedAt(LocalDateTime.now());
        Account saved = accountRepository.save(account);
        logger.info("Created account {} for user {}", accountNumber, username);
        return saved;
    }

    public List<Account> getAccountsForUser(String username) {
        return accountRepository.findByOwnerUsername(username);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
    }

    @Transactional
    public Transaction deposit(String accountNumber, Double amount, String description) {
        Account account = getByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setToAccount(account);
        tx.setDescription(description);
        tx.setTimestamp(LocalDateTime.now());
        Transaction saved = transactionRepository.save(tx);
        logger.info("Deposited {} to {}", amount, accountNumber);
        return saved;
    }

    @Transactional
    public Transaction withdraw(String accountNumber, Double amount, String description) {
        Account account = getByAccountNumber(accountNumber);
        double min = account.getAccountType() == AccountType.SAVINGS ? 100.0 : 0.0;
        if (account.getBalance() - amount < min) {
            throw new InsufficientBalanceException("Insufficient funds or minimum balance would be violated");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setType(TransactionType.WITHDRAWAL);
        tx.setAmount(amount);
        tx.setFromAccount(account);
        tx.setDescription(description);
        tx.setTimestamp(LocalDateTime.now());
        Transaction saved = transactionRepository.save(tx);
        logger.info("Withdrew {} from {}", amount, accountNumber);
        return saved;
    }

    @Transactional
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, Double amount, String description) {
        Account from = getByAccountNumber(fromAccountNumber);
        Account to = getByAccountNumber(toAccountNumber);
        double min = from.getAccountType() == AccountType.SAVINGS ? 100.0 : 0.0;
        if (from.getBalance() - amount < min) {
            throw new InsufficientBalanceException("Insufficient funds for transfer");
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
        Transaction saved = transactionRepository.save(tx);
        logger.info("Transferred {} from {} to {}", amount, fromAccountNumber, toAccountNumber);
        return saved;
    }
}