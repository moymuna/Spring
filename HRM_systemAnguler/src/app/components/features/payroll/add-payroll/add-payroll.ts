import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Payroll } from '../../../../models/payroll.model';
import { PayrollService } from '../../../../services/payroll.service';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-payroll',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-payroll.html',
  styleUrl: './add-payroll.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddPayroll {
  generateForm!: FormGroup;
  months = [
    { value: 1, name: 'January' },
    { value: 2, name: 'February' },
    { value: 3, name: 'March' },
    { value: 4, name: 'April' },
    { value: 5, name: 'May' },
    { value: 6, name: 'June' },
    { value: 7, name: 'July' },
    { value: 8, name: 'August' },
    { value: 9, name: 'September' },
    { value: 10, name: 'October' },
    { value: 11, name: 'November' },
    { value: 12, name: 'December' },
  ];
  years: number[] = [];
  successMessage = '';
  errorMessage = '';
  generating = false;
  employees: EmployeeResponse[] = [];
  result: Payroll | null = null;
  constructor(
    private fb: FormBuilder,
    private payrollService: PayrollService,
    private employeeService: EmployeeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const currentYear = new Date().getFullYear();
    this.years = [currentYear - 1, currentYear, currentYear + 1];
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
    this.generateForm = this.fb.group({
      employeeId: ['', Validators.required],
      month: ['', Validators.required],
      year: [currentYear, Validators.required],
    });
  }
  generatePayroll(): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.result = null;
    if (this.generateForm.invalid) {
      this.generateForm.markAllAsTouched();
      this.errorMessage = 'Please select an employee, month, and year.';
      this.cdr.markForCheck();
      return;
    }
    this.generating = true;
    const { employeeId, month, year } = this.generateForm.value;
    this.payrollService.generatePayroll(employeeId, year, month).subscribe({
      next: (payroll) => {
        this.generating = false;
        this.result = payroll;
        this.successMessage = 'Payroll generated successfully from Salary + Attendance.';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/payroll']);
        }, 1500);
      },
      error: (err) => {
        this.generating = false;
        this.errorMessage = err.error?.message || 'Failed to generate payroll.';
        this.cdr.markForCheck();
      },
    });
  }
}
