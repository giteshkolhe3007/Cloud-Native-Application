// TypeScript types that match the Java model classes on the backend

export interface Account {
  id: number;
  ownerName: string;
  balance: number;
  createdAt: string;
}

export interface Transaction {
  id: number;
  accountId: number;
  type: 'DEPOSIT' | 'WITHDRAWAL';
  amount: number;
  timestamp: string;
}

export type TransactionType = 'deposit' | 'withdraw';
