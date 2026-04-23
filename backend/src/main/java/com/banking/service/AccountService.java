package com.banking.service;

import com.banking.dto.CreateAccountRequest;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * AccountService — contains the business logic for account operations.
 *
 * The Service layer sits between the Controller (handles HTTP) and the
 * Repository (handles database). This separation keeps code organized
 * and makes it easier to test each layer independently.
 *
 * @Service tells Spring this is a service component to manage as a bean.
 */
@Service
public class AccountService {

    // Spring injects the repository automatically (dependency injection)
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new bank account with a zero balance.
     */
    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account(request.getOwnerName());
        return accountRepository.save(account);
    }

    /**
     * Returns a list of all accounts in the database.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Finds an account by its ID.
     * Returns an Optional — it either contains an Account, or is empty if not found.
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Saves an account back to the database (used after updating the balance).
     */
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
