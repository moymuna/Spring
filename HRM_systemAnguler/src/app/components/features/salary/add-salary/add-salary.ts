import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { SalaryService } from '../../../../services/salary.service';
import { EmployeeService } from '../../../../services/employee.service';
import { SalaryGradeService } from '../../../../services/salarygrade.service';
import { SalaryGrade } from '../../../../models/salarygrade.model';
@Component({
  selector: 'app-add-salary',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './add-salary.html',
  styleUrl: './add-salary.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddSalary implements OnInit {
  salaryForm!: FormGroup;
  employees: any[] = [];
  grades: SalaryGrade[] = [];
  successMessage = '';
  errorMessage = '';
  loading = false;
  percentFields: Array<
    'hra' | 'conveyanceAllowance' | 'medicalAllowance' | 'specialAllowance' | 'providentFund'
  > = ['hra', 'conveyanceAllowance', 'medicalAllowance', 'specialAllowance', 'providentFund'];
  percents: Record<string, number | null> = {
    hra: null,
    conveyanceAllowance: null,
    medicalAllowance: null,
    specialAllowance: null,
    providentFund: null,
  };
  constructor(
    private fb: FormBuilder,
    private salaryService: SalaryService,
    private employeeService: EmployeeService,
    private salaryGradeService: SalaryGradeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.initializeForm();
    this.loadEmployees();
    this.loadGrades();
    this.salaryForm.get('basicSalary')?.valueChanges.subscribe(() => {
      this.percentFields.forEach((field) => this.recalculateFromPercent(field));
    });
  }
  loadGrades(): void {
    this.salaryGradeService.getActiveGrades().subscribe({
      next: (data) => {
        this.grades = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.cdr.markForCheck();
      },
    });
  }
  /** Picking a grade drops that grade's whole structure into the form; every amount stays editable. */
  onGradeChange(gradeId: string): void {
    const grade = this.grades.find((g) => String(g.id) === String(gradeId));
    if (!grade) {
      return;
    }
    this.salaryForm.patchValue(
      {
        basicSalary: grade.basicSalary,
        hra: grade.hra,
        conveyanceAllowance: grade.conveyanceAllowance,
        medicalAllowance: grade.medicalAllowance,
        specialAllowance: grade.specialAllowance,
        providentFund: grade.providentFund,
        professionalTax: grade.professionalTax,
        incomeTax: grade.incomeTax,
      },
      { emitEvent: false },
    );
    this.resetPercents();
    this.cdr.markForCheck();
  }
  recalculateFromPercent(
    field:
      'hra' | 'conveyanceAllowance' | 'medicalAllowance' | 'specialAllowance' | 'providentFund',
  ): void {
    const percent = this.percents[field];
    if (percent === null || percent === undefined || percent === ('' as any)) {
      return;
    }
    const basicSalary = Number(this.salaryForm.get('basicSalary')?.value) || 0;
    const amount = Math.round(((basicSalary * percent) / 100) * 100) / 100;
    this.salaryForm.get(field)?.setValue(amount, { emitEvent: false });
    this.cdr.markForCheck();
  }
  onPercentChange(
    field:
      'hra' | 'conveyanceAllowance' | 'medicalAllowance' | 'specialAllowance' | 'providentFund',
    value: string,
  ): void {
    const percent = value === '' ? null : Number(value);
    this.percents[field] = percent;
    this.recalculateFromPercent(field);
  }
  initializeForm(): void {
    this.salaryForm = this.fb.group({
      basicSalary: ['', [Validators.required, Validators.min(0)]],
      hra: [0],
      conveyanceAllowance: [0],
      medicalAllowance: [0],
      specialAllowance: [0],
      providentFund: [0],
      professionalTax: [0],
      incomeTax: [0],
      effectiveFrom: ['', Validators.required],
      effectiveTo: [''],
      active: [true],
      employeeId: ['', Validators.required],
      salaryGradeId: [''],
    });
  }
  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any) => {
        this.employees = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.cdr.markForCheck();
      },
    });
  }
  saveSalary(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.salaryForm.invalid) {
      this.salaryForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.salaryService.createSalary(this.salaryForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Salary added successfully.';
        this.salaryForm.reset({
          hra: 0,
          conveyanceAllowance: 0,
          medicalAllowance: 0,
          specialAllowance: 0,
          providentFund: 0,
          professionalTax: 0,
          incomeTax: 0,
          active: true,
        });
        this.resetPercents();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/salary']);
        }, 1000);
      },
      error: (err) => {
        this.loading = false;
        console.error(err);
        this.errorMessage = err.error?.message || 'Failed to save salary.';
        this.cdr.markForCheck();
      },
    });
  }
  resetForm(): void {
    this.salaryForm.reset({
      hra: 0,
      conveyanceAllowance: 0,
      medicalAllowance: 0,
      specialAllowance: 0,
      providentFund: 0,
      professionalTax: 0,
      incomeTax: 0,
      active: true,
    });
    this.resetPercents();
    this.successMessage = '';
    this.errorMessage = '';
  }
  private resetPercents(): void {
    this.percentFields.forEach((field) => (this.percents[field] = null));
  }
  get f() {
    return this.salaryForm.controls;
  }
  get grossSalary(): number {
    const v = this.salaryForm.value;
    return (
      (Number(v.basicSalary) || 0) +
      (Number(v.hra) || 0) +
      (Number(v.conveyanceAllowance) || 0) +
      (Number(v.medicalAllowance) || 0) +
      (Number(v.specialAllowance) || 0)
    );
  }
  get totalDeductions(): number {
    const v = this.salaryForm.value;
    return (
      (Number(v.providentFund) || 0) + (Number(v.professionalTax) || 0) + (Number(v.incomeTax) || 0)
    );
  }
  get netSalary(): number {
    return this.grossSalary - this.totalDeductions;
  }
}
