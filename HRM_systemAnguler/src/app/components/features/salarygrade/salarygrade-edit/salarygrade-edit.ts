import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SalaryGradeService } from '../../../../services/salarygrade.service';
@Component({
  selector: 'app-salarygrade-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './salarygrade-edit.html',
  styleUrl: './salarygrade-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SalarygradeEdit implements OnInit {
  gradeForm!: FormGroup;
  gradeId!: number;
  successMessage = '';
  errorMessage = '';
  loading = false;
  constructor(
    private fb: FormBuilder,
    private salaryGradeService: SalaryGradeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.gradeForm = this.fb.group({
      gradeNumber: ['', [Validators.required, Validators.min(1)]],
      title: ['', Validators.required],
      basicSalary: ['', [Validators.required, Validators.min(0)]],
      hra: [0],
      conveyanceAllowance: [0],
      medicalAllowance: [0],
      specialAllowance: [0],
      providentFund: [0],
      professionalTax: [0],
      incomeTax: [0],
      active: [true],
    });
    this.gradeId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadGrade();
  }
  loadGrade(): void {
    this.salaryGradeService.getGradeById(this.gradeId).subscribe({
      next: (grade) => {
        this.gradeForm.patchValue(grade);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load the salary grade.';
        this.cdr.markForCheck();
      },
    });
  }
  updateGrade(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.gradeForm.invalid) {
      this.gradeForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.salaryGradeService.updateGrade(this.gradeId, this.gradeForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Salary grade updated successfully.';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/salarygrade']), 800);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Failed to update the salary grade.';
        this.cdr.markForCheck();
      },
    });
  }
  get f() {
    return this.gradeForm.controls;
  }
  get grossMonthly(): number {
    const v = this.gradeForm.value;
    return (
      (Number(v.basicSalary) || 0) +
      (Number(v.hra) || 0) +
      (Number(v.conveyanceAllowance) || 0) +
      (Number(v.medicalAllowance) || 0) +
      (Number(v.specialAllowance) || 0)
    );
  }
  get totalDeductions(): number {
    const v = this.gradeForm.value;
    return (
      (Number(v.providentFund) || 0) + (Number(v.professionalTax) || 0) + (Number(v.incomeTax) || 0)
    );
  }
  get netMonthly(): number {
    return this.grossMonthly - this.totalDeductions;
  }
}
