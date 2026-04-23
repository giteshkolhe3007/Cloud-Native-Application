package com.banking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account entity — represents a bank account in the database.
 *
 * @Entity tells JPA this class maps to a database table.
 * @Table(name = "accounts") sets the table name.
 */
@Entity
@Table(name = "accounts")
public class Account {

    /**
     * Primary key. @GeneratedValue makes the database auto-generate a unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the account owner. Cannot be null.
     */
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    /**
     * The current balance. Defaults to 0.00.
     * BigDecimal is used for money to avoid floating-point precision issues.
     */
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Timestamp of when the account was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * @PrePersist runs automatically before the entity is first saved to the database.
     * We use it to set the createdAt timestamp.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ==========================================
    // Constructors
    // ==========================================

    public Account() {
        // JPA requires a no-argument constructor
    }

    public Account(String ownerName) {
        this.ownerName = ownerName;
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
