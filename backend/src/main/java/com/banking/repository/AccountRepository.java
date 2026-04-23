package com.banking.repository;

import com.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AccountRepository — handles all database operations for Account entities.
 *
 * By extending JpaRepository, Spring automatically provides methods like:
 * - findAll()       → get all accounts
 * - findById(id)    → get one account by ID
 * - save(account)   → create or update an account
 * - deleteById(id)  → delete an account
 *
 * No implementation needed — Spring Data JPA generates the code at runtime!
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // No extra methods needed — JpaRepository covers everything we need
}
