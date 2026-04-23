import { useState, useEffect, useCallback } from 'react';
import { Building2, RefreshCw, WifiOff } from 'lucide-react';
import { Account, Transaction } from './types';
import { getAccounts, getTransactions } from './api/bankingApi';
import AccountForm from './components/AccountForm';
import AccountList from './components/AccountList';
import TransactionPanel from './components/TransactionPanel';

/**
 * App — the root component of the banking dashboard.
 *
 * Manages global state:
 * - accounts: list of all bank accounts
 * - selectedAccount: the account currently being viewed
 * - transactions: transaction history for the selected account
 */
export default function App() {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<Account | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loadingAccounts, setLoadingAccounts] = useState(true);
  const [loadingTx, setLoadingTx] = useState(false);
  const [backendError, setBackendError] = useState(false);

  // Fetch all accounts from the backend on first load
  const fetchAccounts = useCallback(async () => {
    setLoadingAccounts(true);
    setBackendError(false);
    try {
      const data = await getAccounts();
      setAccounts(data);
    } catch {
      setBackendError(true);
    } finally {
      setLoadingAccounts(false);
    }
  }, []);

  useEffect(() => {
    fetchAccounts();
  }, [fetchAccounts]);

  // Fetch transactions whenever the selected account changes
  const fetchTransactions = useCallback(async (accountId: number) => {
    setLoadingTx(true);
    try {
      const data = await getTransactions(accountId);
      setTransactions(data);
    } catch {
      setTransactions([]);
    } finally {
      setLoadingTx(false);
    }
  }, []);

  // Called when user clicks an account in the list
  const handleSelectAccount = (account: Account) => {
    setSelectedAccount(account);
    fetchTransactions(account.id);
  };

  // Called after a new account is created
  const handleAccountCreated = (newAccount: Account) => {
    setAccounts((prev) => [newAccount, ...prev]);
  };

  // Called after a deposit or withdrawal completes
  // Updates the account balance in the list and refreshes transactions
  const handleTransactionComplete = (updatedAccount: Account) => {
    // Update the balance shown in the account list
    setAccounts((prev) =>
      prev.map((a) => (a.id === updatedAccount.id ? updatedAccount : a))
    );
    // Update the selected account's balance
    setSelectedAccount(updatedAccount);
    // Refresh the transaction list
    fetchTransactions(updatedAccount.id);
  };

  return (
    <div className="min-h-screen bg-slate-50">
      {/* Top Navigation Bar */}
      <header className="bg-white border-b border-slate-200 sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-xl bg-blue-600 flex items-center justify-center">
              <Building2 className="w-5 h-5 text-white" />
            </div>
            <div>
              <h1 className="text-base font-bold text-slate-900 leading-tight">ClearBank</h1>
              <p className="text-xs text-slate-400 leading-tight">Dashboard</p>
            </div>
          </div>

          <button
            onClick={fetchAccounts}
            className="flex items-center gap-2 text-xs text-slate-500 hover:text-slate-700 bg-slate-100 hover:bg-slate-200 px-3 py-1.5 rounded-lg transition-colors"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            Refresh
          </button>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-6xl mx-auto px-6 py-8">

        {/* Backend Connection Error Banner */}
        {backendError && (
          <div className="mb-6 flex items-start gap-3 bg-amber-50 border border-amber-200 rounded-2xl p-5">
            <WifiOff className="w-5 h-5 text-amber-500 flex-shrink-0 mt-0.5" />
            <div>
              <p className="text-sm font-semibold text-amber-800">Cannot connect to backend</p>
              <p className="text-xs text-amber-600 mt-0.5">
                Make sure the Spring Boot API is running on{' '}
                <code className="font-mono bg-amber-100 px-1 rounded">http://localhost:8080</code>.
                Run <code className="font-mono bg-amber-100 px-1 rounded">mvn spring-boot:run</code> in the{' '}
                <code className="font-mono bg-amber-100 px-1 rounded">backend/</code> folder.
              </p>
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Panel: Create Account + Account List */}
          <div className="lg:col-span-1 space-y-5">
            <AccountForm onAccountCreated={handleAccountCreated} />

            {loadingAccounts ? (
              <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6 flex items-center justify-center h-32">
                <RefreshCw className="w-5 h-5 text-slate-400 animate-spin" />
              </div>
            ) : (
              <AccountList
                accounts={accounts}
                selectedId={selectedAccount?.id ?? null}
                onSelect={handleSelectAccount}
              />
            )}
          </div>

          {/* Right Panel: Account Detail & Transactions */}
          <div className="lg:col-span-2">
            {selectedAccount ? (
              loadingTx ? (
                <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6 flex items-center justify-center h-48">
                  <RefreshCw className="w-5 h-5 text-slate-400 animate-spin" />
                </div>
              ) : (
                <TransactionPanel
                  account={selectedAccount}
                  transactions={transactions}
                  onTransactionComplete={handleTransactionComplete}
                />
              )
            ) : (
              <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-12 flex flex-col items-center justify-center text-center h-full min-h-64">
                <div className="w-14 h-14 rounded-2xl bg-blue-50 flex items-center justify-center mb-4">
                  <Building2 className="w-7 h-7 text-blue-400" />
                </div>
                <h3 className="text-base font-semibold text-slate-700 mb-1">Select an Account</h3>
                <p className="text-sm text-slate-400 max-w-xs">
                  Choose an account from the list on the left to view its balance and transaction history.
                </p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
