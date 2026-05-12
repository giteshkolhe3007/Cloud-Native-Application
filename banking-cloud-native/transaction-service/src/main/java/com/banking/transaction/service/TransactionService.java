package com.banking.transaction.service;

import com.banking.transaction.model.Transaction;
import com.banking.transaction.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher transactionEventPublisher;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionEventPublisher transactionEventPublisher) {
        this.transactionRepository = transactionRepository;
        this.transactionEventPublisher = transactionEventPublisher;
    }

    @CircuitBreaker(name = "transactionService", fallbackMethod = "saveTransactionFallback")
    @Retry(name = "transactionService")
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getAccountId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accountId is required");
        }
        if (transaction.getType() == null || transaction.getType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type is required");
        }
        BigDecimal amount = transaction.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be greater than zero");
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        transactionEventPublisher.publishTransactionEvent(savedTransaction);
        return savedTransaction;
    }

    @CircuitBreaker(name = "transactionService", fallbackMethod = "getHistoryFallback")
    public List<Transaction> getHistoryByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }

    public Transaction saveTransactionFallback(Transaction transaction, Exception ex) {
        throw new RuntimeException("Transaction service temporarily unavailable. Please try again later.");
    }

    public List<Transaction> getHistoryFallback(Long accountId, Exception ex) {
        return new ArrayList<>();
    }
}
