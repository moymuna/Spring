import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdvanceService } from '../../../../services/advance.service';
import { EmployeeService } from '../../../../services/employee.service';
import { SalaryService } from '../../../../services/salary.service';
import { StorageService } from '../../../../services/storage.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
@Component({
  selector: 'app-advance-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './advance-create.html',
  styleUrl: './advance-create.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdvanceCreate implements OnInit {
  advanceForm!: FormGroup;
  employees: any[] = [];
  successMessage = '';
  errorMessage = '';
  loading = false;
  isEmployeeOnly = false;
  selfEmployeeName = '';
  grossSalary: number | null = null;
  installmentOptions = Array.from({ length: 12 }, (_, i) => i + 1);
  constructor(
    private fb: FormBuilder,
    private advanceService: AdvanceService,
    private employeeService: EmployeeService,
    private salaryService: SalaryService,
    private storage: StorageService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.advanceForm = this.fb.group({
      employeeId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(1)]],
      requestDate: [new Date().toISOString().substring(0, 10), Validators.required],
      requiredByDate: [''],
      installments: [1, [Validators.required, Validators.min(1), Validators.max(12)]],
      reason: ['', Validators.required],
    });
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    if (this.isEmployeeOnly) {
      this.loadSelfEmployee();
    } else {
      this.loadEmployees();
      this.advanceForm
        .get('employeeId')
        ?.valueChanges.subscribe((id) => this.loadGrossSalary(Number(id)));
    }
  }
  loadSelfEmployee(): void {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (emp) => {
        this.selfEmployeeName = emp.fullName;
        this.advanceForm.get('employeeId')?.setValue(emp.id);
        this.loadGrossSalary(emp.id!);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load your employee profile.';
        this.cdr.markForCheck();
      },
    });
  }
  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.cdr.markForCheck();
      },
    });
  }
  /** Shows the ceiling up front so a request is not rejected for being too large. */
  loadGrossSalary(employeeId: number): void {
    if (!employeeId) {
      this.grossSalary = null;
      return;
    }
    this.salaryService.getSalaryByEmployee(employeeId, silentContext()).subscribe({
      next: (salary) => {
        this.grossSalary = salary?.grossMonthly ?? null;
        this.cdr.markForCheck();
      },
      error: () => {
        this.grossSalary = null;
        this.cdr.markForCheck();
      },
    });
  }
  get maxAdvance(): number | null {
    return this.grossSalary === null ? null : this.grossSalary * 3;
  }
  get monthlyDeduction(): number {
    const amount = Number(this.advanceForm?.get('amount')?.value) || 0;
    const installments = Number(this.advanceForm?.get('installments')?.value) || 1;
    return installments > 0 ? amount / installments : 0;
  }
  /** An untouched date input yields '', which the API cannot read as a date. */
  private buildPayload() {
    const value = this.advanceForm.value;
    return { ...value, requiredByDate: value.requiredByDate || null };
  }
  saveAdvance(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.advanceForm.invalid) {
      this.advanceForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.advanceService.saveAdvance(this.buildPayload()).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Advance request submitted successfully.';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/advance']), 900);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Unable to submit the advance request.';
        this.cdr.markForCheck();
      },
    });
  }
  get f() {
    return this.advanceForm.controls;
  }
}
