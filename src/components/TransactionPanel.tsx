import { useState } from 'react';
import { ArrowDownLeft, ArrowUpRight, Clock, Loader, DollarSign } from 'lucide-react';
import { Account, Transaction } from '../types';
import { deposit, withdraw } from '../api/bankingApi';

interface TransactionPanelProps {
  account: Account;
  transactions: Transaction[];
  onTransactionComplete: (updatedAccount: Account) => void;
}

/**
 * TransactionPanel — shows deposit/withdraw controls and the transaction history
 * for the currently selected account.
 */
export default function TransactionPanel({
  account,
  transactions,
  onTransactionComplete,
}: TransactionPanelProps) {
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);

  const formatDate = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  // Shared function for both deposit and withdraw
  const handleTransaction = async (type: 'deposit' | 'withdraw') => {
    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setError('Please enter a valid positive amount.');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Call the appropriate API endpoint
      if (type === 'deposit') {
        await deposit(account.id, parsedAmount);
        setSuccess(`Successfully deposited ${formatCurrency(parsedAmount)}`);
      } else {
        await withdraw(account.id, parsedAmount);
        setSuccess(`Successfully withdrew ${formatCurrency(parsedAmount)}`);
      }

      // Notify the parent to refresh the account data and transactions
      const updatedBalance =
        type === 'deposit'
          ? account.balance + parsedAmount
          : account.balance - parsedAmount;
      onTransactionComplete({ ...account, balance: updatedBalance });
      setAmount('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Transaction failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-5">
      {/* Account Summary Card */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 rounded-2xl p-6 text-white shadow-lg shadow-blue-200">
        <p className="text-blue-200 text-xs font-medium uppercase tracking-wider mb-1">Current Balance</p>
        <p className="text-4xl font-bold tabular-nums mt-1">{formatCurrency(account.balance)}</p>
        <div className="mt-4 pt-4 border-t border-blue-500 flex items-center gap-2">
          <div className="w-7 h-7 rounded-full bg-blue-500 flex items-center justify-center text-xs font-bold">
            {account.ownerName.charAt(0).toUpperCase()}
          </div>
          <div>
            <p className="text-sm font-medium">{account.ownerName}</p>
            <p className="text-blue-300 text-xs">Account #{account.id}</p>
          </div>
        </div>
      </div>

      {/* Deposit / Withdraw Controls */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
        <div className="flex items-center gap-3 mb-5">
          <div className="w-9 h-9 rounded-xl bg-sky-50 flex items-center justify-center">
            <DollarSign className="w-5 h-5 text-sky-600" />
          </div>
          <h3 className="text-base font-semibold text-slate-800">Quick Transfer</h3>
        </div>

        <div className="space-y-4">
          {/* Amount Input */}
          <div>
            <label className="block text-xs font-medium text-slate-500 mb-1.5 uppercase tracking-wider">
              Amount (USD)
            </label>
            <div className="relative">
              <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">$</span>
              <input
                type="number"
                value={amount}
                onChange={(e) => { setAmount(e.target.value); setError(''); setSuccess(''); }}
                placeholder="0.00"
                min="0.01"
                step="0.01"
                className="w-full pl-8 pr-4 py-2.5 text-sm rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                disabled={loading}
              />
            </div>
          </div>

          {/* Error and Success Messages */}
          {error && (
            <p className="text-xs text-red-500 bg-red-50 border border-red-100 rounded-lg px-3 py-2">
              {error}
            </p>
          )}
          {success && (
            <p className="text-xs text-emerald-600 bg-emerald-50 border border-emerald-100 rounded-lg px-3 py-2">
              {success}
            </p>
          )}

          {/* Action Buttons */}
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={() => handleTransaction('deposit')}
              disabled={loading || !amount}
              className="flex items-center justify-center gap-2 px-4 py-2.5 bg-emerald-500 hover:bg-emerald-600 disabled:bg-emerald-200 text-white text-sm font-medium rounded-xl transition-colors"
            >
              {loading ? (
                <Loader className="w-4 h-4 animate-spin" />
              ) : (
                <ArrowDownLeft className="w-4 h-4" />
              )}
              Deposit
            </button>

            <button
              onClick={() => handleTransaction('withdraw')}
              disabled={loading || !amount}
              className="flex items-center justify-center gap-2 px-4 py-2.5 bg-rose-500 hover:bg-rose-600 disabled:bg-rose-200 text-white text-sm font-medium rounded-xl transition-colors"
            >
              {loading ? (
                <Loader className="w-4 h-4 animate-spin" />
              ) : (
                <ArrowUpRight className="w-4 h-4" />
              )}
              Withdraw
            </button>
          </div>
        </div>
      </div>

      {/* Transaction History */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
        <div className="flex items-center gap-3 mb-5">
          <div className="w-9 h-9 rounded-xl bg-amber-50 flex items-center justify-center">
            <Clock className="w-5 h-5 text-amber-600" />
          </div>
          <h3 className="text-base font-semibold text-slate-800">Transaction History</h3>
        </div>

        {transactions.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-sm text-slate-400">No transactions yet.</p>
            <p className="text-xs text-slate-300 mt-1">Make a deposit to get started.</p>
          </div>
        ) : (
          <ul className="space-y-2">
            {transactions.map((tx) => {
              const isDeposit = tx.type === 'DEPOSIT';
              return (
                <li
                  key={tx.id}
                  className="flex items-center justify-between px-4 py-3 rounded-xl bg-slate-50 hover:bg-slate-100 transition-colors"
                >
                  <div className="flex items-center gap-3">
                    {/* Icon shows deposit (green/down) or withdrawal (red/up) */}
                    <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${
                      isDeposit ? 'bg-emerald-100' : 'bg-rose-100'
                    }`}>
                      {isDeposit ? (
                        <ArrowDownLeft className="w-4 h-4 text-emerald-600" />
                      ) : (
                        <ArrowUpRight className="w-4 h-4 text-rose-500" />
                      )}
                    </div>
                    <div>
                      <p className="text-sm font-medium text-slate-700 capitalize">
                        {tx.type.charAt(0) + tx.type.slice(1).toLowerCase()}
                      </p>
                      <p className="text-xs text-slate-400">{formatDate(tx.timestamp)}</p>
                    </div>
                  </div>
                  {/* Amount: green for deposit, red for withdrawal */}
                  <p className={`text-sm font-semibold tabular-nums ${
                    isDeposit ? 'text-emerald-600' : 'text-rose-500'
                  }`}>
                    {isDeposit ? '+' : '-'}{formatCurrency(tx.amount)}
                  </p>
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
}
