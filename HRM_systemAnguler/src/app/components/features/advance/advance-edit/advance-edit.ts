import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AdvanceService } from '../../../../services/advance.service';
import { EmployeeService } from '../../../../services/employee.service';
@Component({
  selector: 'app-advance-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './advance-edit.html',
  styleUrl: './advance-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdvanceEdit implements OnInit {
  advanceForm!: FormGroup;
  advanceId!: number;
  employees: any[] = [];
  successMessage = '';
  errorMessage = '';
  loading = false;
  installmentOptions = Array.from({ length: 12 }, (_, i) => i + 1);
  constructor(
    private fb: FormBuilder,
    private advanceService: AdvanceService,
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.advanceForm = this.fb.group({
      employeeId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(1)]],
      requestDate: ['', Validators.required],
      requiredByDate: [''],
      installments: [1, [Validators.required, Validators.min(1), Validators.max(12)]],
      reason: ['', Validators.required],
    });
    this.advanceId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadEmployees();
    this.loadAdvance();
  }
  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.cdr.markForCheck();
      },
    });
  }
  loadAdvance(): void {
    this.advanceService.getAdvance(this.advanceId).subscribe({
      next: (advance) => {
        this.advanceForm.patchValue({
          employeeId: advance.employeeId,
          amount: advance.amount,
          requestDate: advance.requestDate,
          requiredByDate: advance.requiredByDate ?? '',
          installments: advance.installments,
          reason: advance.reason,
        });
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load the advance request.';
        this.cdr.markForCheck();
      },
    });
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
  updateAdvance(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.advanceForm.invalid) {
      this.advanceForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.advanceService.updateAdvance(this.advanceId, this.buildPayload()).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Advance request updated successfully.';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/advance']), 900);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Unable to update the advance request.';
        this.cdr.markForCheck();
      },
    });
  }
  get f() {
    return this.advanceForm.controls;
  }
}
