import { Account, Transaction } from '../types';

// Base URL of the Spring Boot backend
const BASE_URL = 'http://localhost:8080/api';

// Helper function to handle API errors consistently
async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Request failed with status ${response.status}`);
  }
  return response.json();
}

// ==========================================
// Account API calls
// ==========================================

/**
 * Creates a new bank account with the given owner name.
 * Calls POST /api/accounts
 */
export async function createAccount(ownerName: string): Promise<Account> {
  const response = await fetch(`${BASE_URL}/accounts`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ownerName }),
  });
  return handleResponse<Account>(response);
}

/**
 * Fetches all bank accounts.
 * Calls GET /api/accounts
 */
export async function getAccounts(): Promise<Account[]> {
  const response = await fetch(`${BASE_URL}/accounts`);
  return handleResponse<Account[]>(response);
}

/**
 * Fetches a single account by ID.
 * Calls GET /api/accounts/{id}
 */
export async function getAccount(id: number): Promise<Account> {
  const response = await fetch(`${BASE_URL}/accounts/${id}`);
  return handleResponse<Account>(response);
}

// ==========================================
// Transaction API calls
// ==========================================

/**
 * Deposits an amount into the specified account.
 * Calls POST /api/transactions/deposit
 */
export async function deposit(accountId: number, amount: number): Promise<Transaction> {
  const response = await fetch(`${BASE_URL}/transactions/deposit`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ accountId, amount }),
  });
  return handleResponse<Transaction>(response);
}

/**
 * Withdraws an amount from the specified account.
 * Calls POST /api/transactions/withdraw
 */
export async function withdraw(accountId: number, amount: number): Promise<Transaction> {
  const response = await fetch(`${BASE_URL}/transactions/withdraw`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ accountId, amount }),
  });
  return handleResponse<Transaction>(response);
}

/**
 * Fetches transaction history for an account.
 * Calls GET /api/transactions/{accountId}
 */
export async function getTransactions(accountId: number): Promise<Transaction[]> {
  const response = await fetch(`${BASE_URL}/transactions/${accountId}`);
  return handleResponse<Transaction[]>(response);
}
