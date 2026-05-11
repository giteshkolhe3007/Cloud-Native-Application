package com.banking.account.service;

import com.banking.account.model.Account;
import com.banking.account.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account, String userEmailHeader) {
        if (account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accountNumber is required");
        }
        if (account.getAccountType() == null || account.getAccountType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accountType is required");
        }

        if (account.getOwnerEmail() == null || account.getOwnerEmail().isBlank()) {
            account.setOwnerEmail(userEmailHeader);
        }
        if (account.getOwnerEmail() == null || account.getOwnerEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerEmail is required");
        }
        if (userEmailHeader != null && !userEmailHeader.isBlank() && !userEmailHeader.equalsIgnoreCase(account.getOwnerEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Header email does not match ownerEmail");
        }

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial balance cannot be negative");
        }
        return accountRepository.save(account);
    }

    public Account getAccount(Long id, String userEmailHeader) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        enforceOwnerAccess(account, userEmailHeader);
        return account;
    }

    public List<Account> getAllAccounts(String userEmailHeader) {
        if (userEmailHeader != null && !userEmailHeader.isBlank()) {
            return accountRepository.findByOwnerEmail(userEmailHeader);
        }
        return accountRepository.findAll();
    }

    public Account deposit(Long id, BigDecimal amount, String userEmailHeader) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be greater than zero");
        }
        Account account = getAccount(id, userEmailHeader);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, BigDecimal amount, String userEmailHeader) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdraw amount must be greater than zero");
        }
        Account account = getAccount(id, userEmailHeader);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    private void enforceOwnerAccess(Account account, String userEmailHeader) {
        if (userEmailHeader != null && !userEmailHeader.isBlank()
                && !userEmailHeader.equalsIgnoreCase(account.getOwnerEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this account");
        }
    }
}
