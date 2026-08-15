import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { SalaryGradeService } from '../../../../services/salarygrade.service';
@Component({
  selector: 'app-salarygrade-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './salarygrade-add.html',
  styleUrl: './salarygrade-add.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SalarygradeAdd implements OnInit {
  gradeForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  loading = false;
  /** Grades already in use — a grade number is unique, so these can't be reused. */
  takenGrades: number[] = [];
  constructor(
    private fb: FormBuilder,
    private salaryGradeService: SalaryGradeService,
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
    // Keep the title in step with the grade unless the admin has typed their own.
    this.gradeForm.get('gradeNumber')?.valueChanges.subscribe((value) => {
      const title = this.gradeForm.get('title');
      if (value && (!title?.value || /^Grade \d+$/.test(title.value))) {
        title?.setValue(`Grade ${value}`, { emitEvent: false });
      }
      this.cdr.markForCheck();
    });
    this.loadTakenGrades();
  }
  /** Pre-fills the next free grade so adding a step is one click. */
  loadTakenGrades(): void {
    this.salaryGradeService.getAllGrades().subscribe({
      next: (grades) => {
        this.takenGrades = grades.map((g) => g.gradeNumber).sort((a, b) => a - b);
        const next = (this.takenGrades[this.takenGrades.length - 1] ?? 0) + 1;
        this.gradeForm.patchValue({ gradeNumber: next, title: `Grade ${next}` });
        this.cdr.markForCheck();
      },
      error: () => this.cdr.markForCheck(),
    });
  }
  get gradeTaken(): boolean {
    const value = Number(this.gradeForm?.get('gradeNumber')?.value);
    return !!value && this.takenGrades.includes(value);
  }
  saveGrade(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.gradeForm.invalid) {
      this.gradeForm.markAllAsTouched();
      return;
    }
    if (this.gradeTaken) {
      this.errorMessage = `Grade ${this.gradeForm.value.gradeNumber} already exists. Pick an unused number.`;
      return;
    }
    this.loading = true;
    this.salaryGradeService.createGrade(this.gradeForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Salary grade added successfully.';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/salarygrade']), 800);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Failed to save the salary grade.';
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
