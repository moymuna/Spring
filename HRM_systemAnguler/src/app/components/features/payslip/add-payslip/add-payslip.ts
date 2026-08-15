import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PayslipService } from '../../../../services/payslip.service';
import { PayrollService } from '../../../../services/payroll.service';
import { Router } from '@angular/router';
import { PayrollStatus } from '../../../../models/payslip.model';
import { Payroll } from '../../../../models/payroll.model';
import { ToastService } from '../../../../services/toast.service';
const MONTH_NAMES = [
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
@Component({
  selector: 'app-add-payslip',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-payslip.html',
  styleUrl: './add-payslip.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddPayslip implements OnInit {
  payslipForm!: FormGroup;
  statuses = Object.values(PayrollStatus);
  payrolls: Payroll[] = [];
  selectedPayrollId: number | '' = '';
  constructor(
    private fb: FormBuilder,
    private payslipService: PayslipService,
    private payrollService: PayrollService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.payrollService.getAllPayrolls().subscribe({
      next: (data) => {
        this.payrolls = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.toast.error('Could not load payroll records.');
      },
    });
    this.payslipForm = this.fb.group({
      month: ['', Validators.required],
      year: ['', Validators.required],
      grossSalary: ['', Validators.required],
      totalDeductions: ['', Validators.required],
      netSalary: ['', Validators.required],
      paidDays: [''],
      lopDays: [''],
      status: [PayrollStatus.DRAFT, Validators.required],
      generatedAt: [''],
      paidAt: [''],
      employeeId: ['', Validators.required],
      payrollId: ['', Validators.required],
    });
  }
  monthName(month: number): string {
    return MONTH_NAMES[month - 1] ?? String(month);
  }
  onPayrollSelected(): void {
    if (!this.selectedPayrollId) {
      return;
    }
    const payroll = this.payrolls.find((p) => p.id === Number(this.selectedPayrollId));
    if (!payroll) {
      return;
    }
    this.payslipForm.patchValue({
      month: payroll.month,
      year: payroll.year,
      grossSalary: payroll.grossSalary,
      totalDeductions: payroll.totalDeductions,
      netSalary: payroll.netSalary,
      paidDays: payroll.paidDays,
      lopDays: payroll.lopDays,
      generatedAt: payroll.generatedAt,
      employeeId: payroll.employeeId,
      payrollId: payroll.id,
    });
    this.cdr.markForCheck();
  }
  save() {
    if (this.payslipForm.invalid) {
      this.toast.error('Fill all required fields');
      return;
    }
    this.payslipService.createPayslip(this.payslipForm.value).subscribe({
      next: () => {
        this.toast.success('Payslip saved successfully');
        this.router.navigate(['/payslip']);
      },
      error: (err) => {
        console.log(err);
        this.toast.error(err.error?.message || 'Failed to save payslip');
      },
    });
  }
}
