export enum PayrollStatus {
  DRAFT = 'DRAFT',
  PROCESSED = 'PROCESSED',
  PAID = 'PAID',
  FAILED = 'FAILED',
}
export interface Payroll {
  id?: number;
  month: number;
  year: number;
  grossSalary: number;
  totalDeductions: number;
  netSalary: number;
  paidDays: number;
  lopDays: number;
  advanceDeduction?: number;
  status: PayrollStatus;
  generatedAt?: string;
  paidAt?: string;
  employeeId: number;
  employeeName?: string;
}
