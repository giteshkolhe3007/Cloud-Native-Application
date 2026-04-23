package com.banking.service;

import com.banking.dto.TransactionRequest;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * TransactionService — handles deposit and withdrawal business logic.
 *
 * This is where we enforce rules like:
 * - Amount must be positive
 * - Account must exist
 * - Withdrawal cannot exceed the current balance
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    /**
     * Deposits money into an account.
     * Steps:
     * 1. Validate the amount is positive
     * 2. Find the account
     * 3. Add the amount to the balance
     * 4. Save the updated balance
     * 5. Record the transaction
     */
    public Transaction deposit(TransactionRequest request) {
        // Validate amount
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be greater than zero");
        }

        // Find the account or throw a 404 error if not found
        Account account = accountService.getAccountById(request.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found with ID: " + request.getAccountId()));

        // Add amount to balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountService.saveAccount(account);

        // Record the transaction
        Transaction transaction = new Transaction(account.getId(), "DEPOSIT", request.getAmount());
        return transactionRepository.save(transaction);
    }

    /**
     * Withdraws money from an account.
     * Steps:
     * 1. Validate the amount is positive
     * 2. Find the account
     * 3. Check sufficient funds
     * 4. Subtract the amount from the balance
     * 5. Save the updated balance
     * 6. Record the transaction
     */
    public Transaction withdraw(TransactionRequest request) {
        // Validate amount
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal amount must be greater than zero");
        }

        // Find the account
        Account account = accountService.getAccountById(request.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found with ID: " + request.getAccountId()));

        // Check for sufficient funds
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient funds. Available balance: " + account.getBalance());
        }

        // Subtract amount from balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountService.saveAccount(account);

        // Record the transaction
        Transaction transaction = new Transaction(account.getId(), "WITHDRAWAL", request.getAmount());
        return transactionRepository.save(transaction);
    }

    /**
     * Returns all transactions for a given account, newest first.
     */
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }
}
