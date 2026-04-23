package com.banking.repository;

import com.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransactionRepository — handles all database operations for Transaction entities.
 *
 * We add one custom method: findByAccountId.
 * Spring Data JPA reads the method name and automatically writes the SQL query:
 * SELECT * FROM transactions WHERE account_id = ?
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for a specific account, ordered newest first.
     * Spring auto-generates the query from the method name.
     */
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}
