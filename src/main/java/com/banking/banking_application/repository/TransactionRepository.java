package com.banking.banking_application.repository;

import com.banking.banking_application.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByFromAccount_AccountNumberOrToAccount_AccountNumber(String from, String to,
            Pageable pageable);

    Page<Transaction> findByFromAccount_AccountNumber(String fromAccountNumber, Pageable pageable);

    Page<Transaction> findByToAccount_AccountNumber(String toAccountNumber, Pageable pageable);
}