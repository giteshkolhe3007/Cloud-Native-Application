package com.banking.controller;

import com.banking.dto.TransactionRequest;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransactionController — handles all HTTP requests related to transactions.
 * Supports depositing, withdrawing, and viewing transaction history.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/transactions/deposit
     * Deposits money into the specified account.
     * Expects JSON body: { "accountId": 1, "amount": 500.00 }
     */
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.deposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * POST /api/transactions/withdraw
     * Withdraws money from the specified account.
     * Expects JSON body: { "accountId": 1, "amount": 200.00 }
     */
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.withdraw(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * GET /api/transactions/{accountId}
     * Returns the transaction history for a specific account (newest first).
     */
    @GetMapping("/{accountId}")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}
