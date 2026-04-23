package com.banking.dto;

import java.math.BigDecimal;

/**
 * DTO for deposit and withdrawal requests.
 * Both operations need an account ID and an amount.
 */
public class TransactionRequest {

    private Long accountId;
    private BigDecimal amount;

    public TransactionRequest() {}

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
