package com.banking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity — records every deposit or withdrawal made on an account.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the account this transaction belongs to.
     */
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /**
     * The type of transaction: "DEPOSIT" or "WITHDRAWAL".
     */
    @Column(nullable = false)
    private String type;

    /**
     * The amount of money involved in the transaction.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * When the transaction occurred.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    // ==========================================
    // Constructors
    // ==========================================

    public Transaction() {}

    public Transaction(Long accountId, String type, BigDecimal amount) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
