package com.banking.banking_application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.banking.banking_application.model.User;
import com.banking.banking_application.model.Account;
import com.banking.banking_application.model.AccountType;
import com.banking.banking_application.repository.UserRepository;
import com.banking.banking_application.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootApplication
public class BankingApplication {
    private static final Logger logger = LoggerFactory.getLogger(BankingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    // Sample data initialization for admin and test user
    @Bean
    public CommandLineRunner dataInitializer(UserRepository userRepository, AccountRepository accountRepository,
            BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@bank.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
                admin.setEnabled(true);
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);

                User user = new User();
                user.setUsername("user");
                user.setEmail("user@bank.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of("ROLE_USER"));
                user.setEnabled(true);
                user.setCreatedAt(LocalDateTime.now());
                userRepository.save(user);

                Account a1 = new Account();
                a1.setAccountNumber("ACC1000001");
                a1.setAccountType(AccountType.SAVINGS);
                a1.setBalance(1000.0);
                a1.setOwner(user);
                a1.setCreatedAt(LocalDateTime.now());
                accountRepository.save(a1);

                Account a2 = new Account();
                a2.setAccountNumber("ACC1000002");
                a2.setAccountType(AccountType.CHECKING);
                a2.setBalance(500.0);
                a2.setOwner(admin);
                a2.setCreatedAt(LocalDateTime.now());
                accountRepository.save(a2);

                logger.info("Initialized sample users and accounts");
            }
        };
    }
}