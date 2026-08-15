import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PayrollStatus } from '../../../../models/payroll.model';
import { PayrollService } from '../../../../services/payroll.service';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-edit-payroll',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-payroll.html',
  styleUrl: './edit-payroll.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPayroll implements OnInit {
  payrollForm!: FormGroup;
  payrollId!: number;
  statuses = Object.values(PayrollStatus);
  successMessage = '';
  errorMessage = '';
  employees: EmployeeResponse[] = [];
  constructor(
    private fb: FormBuilder,
    private payrollService: PayrollService,
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
    this.payrollId = Number(this.route.snapshot.paramMap.get('id'));
    this.payrollForm = this.fb.group({
      month: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      year: ['', Validators.required],
      grossSalary: ['', Validators.required],
      totalDeductions: ['', Validators.required],
      netSalary: ['', Validators.required],
      paidDays: ['', Validators.required],
      lopDays: [0],
      status: ['', Validators.required],
      employeeId: ['', Validators.required],
      generatedAt: [''],
      paidAt: [''],
    });
    this.loadPayroll();
  }
  loadPayroll() {
    this.payrollService.getPayrollById(this.payrollId).subscribe({
      next: (data) => {
        this.payrollForm.patchValue(data);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  updatePayroll() {
    if (this.payrollForm.invalid) {
      this.errorMessage = 'Please fill all fields';
      return;
    }
    this.payrollService.updatePayroll(this.payrollId, this.payrollForm.value).subscribe({
      next: () => {
        this.successMessage = 'Payroll updated successfully';
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Payroll update failed';
        this.cdr.markForCheck();
      },
    });
  }
}
