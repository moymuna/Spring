export interface Salary {
  id?: number;
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
  effectiveFrom: string;
  effectiveTo?: string;
  active: boolean;
  netMonthly?: number;
  employeeId: number;
  employeeName?: string;
  employeeCode?: string;
  salaryGradeId?: number;
  gradeNumber?: number;
  gradeTitle?: string;
}
