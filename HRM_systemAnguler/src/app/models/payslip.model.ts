export enum PayrollStatus {
  DRAFT = 'DRAFT',
  PROCESSED = 'PROCESSED',
  PAID = 'PAID',
  FAILED = 'FAILED',
}
export interface Payslip {
  id?: number;
  month: number;
  year: number;
  grossSalary: number;
  totalDeductions: number;
  netSalary: number;
  paidDays: number;
  lopDays: number;
  unpaidLeaveDays?: number;
  leaveDeduction?: number;
  advanceDeduction?: number;
  lopDeduction?: number;
  providentFund?: number;
  professionalTax?: number;
  incomeTax?: number;
  status: PayrollStatus;
  generatedAt?: string;
  paidAt?: string;
  employeeId: number;
  employeeName?: string;
  bankName?: string;
  bankAccountNumber?: string;
  payrollId: number;
}
