package com.banking.account.controller;

import com.banking.account.model.Account;
import com.banking.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody Account account,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        Account created = accountService.createAccount(account, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public Account getAccount(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return accountService.getAccount(id, userEmail);
    }

    @GetMapping
    public List<Account> getAllAccounts(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return accountService.getAllAccounts(userEmail);
    }

    @PostMapping("/{id}/deposit")
    public Account deposit(
            @PathVariable Long id,
            @RequestBody AmountRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return accountService.deposit(id, request.amount, userEmail);
    }

    @PostMapping("/{id}/withdraw")
    public Account withdraw(
            @PathVariable Long id,
            @RequestBody AmountRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return accountService.withdraw(id, request.amount, userEmail);
    }

    public static class AmountRequest {
        public BigDecimal amount;
    }
}
