import { User, TrendingUp } from 'lucide-react';
import { Account } from '../types';

interface AccountListProps {
  accounts: Account[];
  selectedId: number | null;
  onSelect: (account: Account) => void;
}

/**
 * AccountList — displays all bank accounts as a scrollable list.
 * Clicking an account selects it to view details.
 */
export default function AccountList({ accounts, selectedId, onSelect }: AccountListProps) {
  // Helper to format numbers as currency (e.g., $1,234.56)
  const formatCurrency = (amount: number) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
      <div className="flex items-center justify-between mb-5">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-emerald-50 flex items-center justify-center">
            <TrendingUp className="w-5 h-5 text-emerald-600" />
          </div>
          <h2 className="text-base font-semibold text-slate-800">Accounts</h2>
        </div>
        <span className="text-xs font-medium text-slate-400 bg-slate-100 px-2.5 py-1 rounded-full">
          {accounts.length} {accounts.length === 1 ? 'account' : 'accounts'}
        </span>
      </div>

      {accounts.length === 0 ? (
        <div className="text-center py-10">
          <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-3">
            <User className="w-6 h-6 text-slate-400" />
          </div>
          <p className="text-sm text-slate-400">No accounts yet.</p>
          <p className="text-xs text-slate-300 mt-1">Create one above to get started.</p>
        </div>
      ) : (
        <ul className="space-y-2">
          {accounts.map((account) => {
            const isSelected = account.id === selectedId;
            return (
              <li key={account.id}>
                <button
                  onClick={() => onSelect(account)}
                  className={`w-full text-left rounded-xl px-4 py-3.5 transition-all ${
                    isSelected
                      ? 'bg-blue-600 text-white shadow-md shadow-blue-200'
                      : 'bg-slate-50 hover:bg-slate-100 text-slate-800'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold ${
                        isSelected ? 'bg-blue-500 text-white' : 'bg-white text-slate-600 shadow-sm'
                      }`}>
                        {account.ownerName.charAt(0).toUpperCase()}
                      </div>
                      <div>
                        <p className={`text-sm font-medium ${isSelected ? 'text-white' : 'text-slate-800'}`}>
                          {account.ownerName}
                        </p>
                        <p className={`text-xs mt-0.5 ${isSelected ? 'text-blue-200' : 'text-slate-400'}`}>
                          ID #{account.id}
                        </p>
                      </div>
                    </div>
                    <p className={`text-sm font-semibold tabular-nums ${isSelected ? 'text-white' : 'text-slate-700'}`}>
                      {formatCurrency(account.balance)}
                    </p>
                  </div>
                </button>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
}
