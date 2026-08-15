import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Salary } from '../../../../models/salary.model';
import { Payroll } from '../../../../models/payroll.model';
import { Advance } from '../../../../models/advance.model';
import { SalaryService } from '../../../../services/salary.service';
import { PayrollService } from '../../../../services/payroll.service';
import { AdvanceService } from '../../../../services/advance.service';
import { EmployeeService } from '../../../../services/employee.service';
import { StorageService } from '../../../../services/storage.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
/**
 * Read-only view of an employee's own pay: the salary structure the admin set,
 * the payroll summary per month, and any outstanding advance.
 */
@Component({
  selector: 'app-my-salary',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-salary.html',
  styleUrl: './my-salary.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MySalary implements OnInit {
  salary: Salary | null = null;
  payrolls: Payroll[] = [];
  advances: Advance[] = [];
  employeeName = '';
  employeeCode = '';
  salaryMessage = '';
  errorMessage = '';
  monthNames = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
  ];
  constructor(
    private salaryService: SalaryService,
    private payrollService: PayrollService,
    private advanceService: AdvanceService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      this.errorMessage = 'Could not identify the signed-in user.';
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (employee) => {
        this.employeeName = employee.fullName;
        this.employeeCode = employee.employeeCode ?? '';
        this.loadSalary(employee.id!);
        this.loadPayrolls(employee.id!);
        this.loadAdvances(employee.id!);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load your employee profile.';
        this.cdr.markForCheck();
      },
    });
  }
  loadSalary(employeeId: number): void {
    this.salaryService.getSalaryByEmployee(employeeId, silentContext()).subscribe({
      next: (salary) => {
        this.salary = salary;
        this.cdr.markForCheck();
      },
      error: () => {
        this.salaryMessage = 'No salary structure has been assigned to you yet.';
        this.cdr.markForCheck();
      },
    });
  }
  loadPayrolls(employeeId: number): void {
    this.payrollService.getPayrollsByEmployee(employeeId, silentContext()).subscribe({
      next: (payrolls) => {
        this.payrolls = payrolls;
        this.cdr.markForCheck();
      },
      error: () => {
        this.payrolls = [];
        this.cdr.markForCheck();
      },
    });
  }
  loadAdvances(employeeId: number): void {
    this.advanceService.getAdvancesByEmployee(employeeId).subscribe({
      next: (advances) => {
        this.advances = advances.filter(
          (a) => a.status === 'APPROVED' || a.status === 'PAID' || a.status === 'PENDING',
        );
        this.cdr.markForCheck();
      },
      error: () => {
        this.advances = [];
        this.cdr.markForCheck();
      },
    });
  }
  monthName(month: number): string {
    return this.monthNames[month - 1] ?? String(month);
  }
  get netMonthly(): number {
    if (!this.salary) {
      return 0;
    }
    return (this.salary.grossMonthly ?? 0) - (this.salary.totalDeductions ?? 0);
  }
  get totalOutstandingAdvance(): number {
    return this.advances
      .filter((a) => a.status === 'PAID')
      .reduce((sum, a) => sum + (a.outstandingAmount ?? 0), 0);
  }
  printStructure(): void {
    window.print();
  }
}
