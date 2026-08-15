export interface SalaryGrade {
  id?: number;
  gradeNumber: number;
  title: string;
  basicSalary: number;
  hra: number;
  conveyanceAllowance: number;
  medicalAllowance: number;
  specialAllowance: number;
  providentFund: number;
  professionalTax: number;
  incomeTax: number;
  grossMonthly?: number;
  totalDeductions?: number;
  netMonthly?: number;
  active: boolean;
}
