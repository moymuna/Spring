export interface Advance {
  id?: number;
  amount: number;
  requestDate: string;
  requiredByDate?: string;
  installments: number;
  monthlyDeduction?: number;
  recoveredAmount?: number;
  outstandingAmount?: number;
  reason: string;
  status?: string;
  decidedAt?: string;
  rejectionReason?: string;
  employeeId: number;
  employeeName?: string;
  employeeCode?: string;
}
