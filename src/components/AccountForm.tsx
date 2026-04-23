import { useState } from 'react';
import { UserPlus, Loader } from 'lucide-react';
import { createAccount } from '../api/bankingApi';
import { Account } from '../types';

interface AccountFormProps {
  onAccountCreated: (account: Account) => void;
}

/**
 * AccountForm — a simple form to create a new bank account.
 * Asks for the owner's name and calls the backend API on submit.
 */
export default function AccountForm({ onAccountCreated }: AccountFormProps) {
  const [ownerName, setOwnerName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!ownerName.trim()) return;

    setLoading(true);
    setError('');

    try {
      // Call the API to create the account
      const newAccount = await createAccount(ownerName.trim());
      onAccountCreated(newAccount);
      setOwnerName(''); // Clear the form after success
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create account');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
      <div className="flex items-center gap-3 mb-5">
        <div className="w-9 h-9 rounded-xl bg-blue-50 flex items-center justify-center">
          <UserPlus className="w-5 h-5 text-blue-600" />
        </div>
        <h2 className="text-base font-semibold text-slate-800">New Account</h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-medium text-slate-500 mb-1.5 uppercase tracking-wider">
            Account Holder Name
          </label>
          <input
            type="text"
            value={ownerName}
            onChange={(e) => setOwnerName(e.target.value)}
            placeholder="e.g. Jane Smith"
            className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            disabled={loading}
          />
        </div>

        {error && (
          <p className="text-xs text-red-500 bg-red-50 border border-red-100 rounded-lg px-3 py-2">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading || !ownerName.trim()}
          className="w-full flex items-center justify-center gap-2 px-4 py-2.5 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white text-sm font-medium rounded-xl transition-colors"
        >
          {loading ? (
            <Loader className="w-4 h-4 animate-spin" />
          ) : (
            <UserPlus className="w-4 h-4" />
          )}
          {loading ? 'Creating...' : 'Open Account'}
        </button>
      </form>
    </div>
  );
}
