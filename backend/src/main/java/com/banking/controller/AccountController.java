package com.banking.controller;

import com.banking.dto.CreateAccountRequest;
import com.banking.model.Account;
import com.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * AccountController — handles all HTTP requests related to bank accounts.
 *
 * @RestController combines @Controller and @ResponseBody, meaning every method
 * automatically returns JSON instead of a view template.
 *
 * @RequestMapping("/api/accounts") sets the base URL path for all methods here.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * POST /api/accounts
     * Creates a new bank account.
     * Expects a JSON body: { "ownerName": "Alice" }
     *
     * @RequestBody tells Spring to parse the JSON request body into a CreateAccountRequest object.
     * ResponseEntity lets us control the HTTP status code (201 Created here).
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        if (request.getOwnerName() == null || request.getOwnerName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner name is required");
        }
        Account created = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/accounts
     * Returns a list of all bank accounts.
     */
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    /**
     * GET /api/accounts/{id}
     * Returns a single account by its ID.
     *
     * @PathVariable extracts the {id} part from the URL.
     */
    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found with ID: " + id));
    }
}
