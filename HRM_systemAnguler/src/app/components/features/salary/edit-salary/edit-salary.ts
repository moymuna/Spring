import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SalaryService } from '../../../../services/salary.service';
import { EmployeeService } from '../../../../services/employee.service';
import { SalaryGradeService } from '../../../../services/salarygrade.service';
import { SalaryGrade } from '../../../../models/salarygrade.model';
@Component({
  selector: 'app-edit-salary',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './edit-salary.html',
  styleUrl: './edit-salary.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditSalary implements OnInit {
  salaryForm!: FormGroup;
  salaryId!: number;
  employees: any[] = [];
  grades: SalaryGrade[] = [];
  successMessage = '';
  errorMessage = '';
  loading = false;
  constructor(
    private fb: FormBuilder,
    private salaryService: SalaryService,
    private employeeService: EmployeeService,
    private salaryGradeService: SalaryGradeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.salaryId = Number(this.route.snapshot.paramMap.get('id'));
    this.createForm();
    this.loadEmployees();
    this.loadGrades();
    this.loadSalary();
  }
  loadGrades() {
    this.salaryGradeService.getActiveGrades().subscribe({
      next: (data) => {
        this.grades = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  /** Picking a grade drops that grade's whole structure into the form; every amount stays editable. */
  onGradeChange(gradeId: string) {
    const grade = this.grades.find((g) => String(g.id) === String(gradeId));
    if (!grade) {
      return;
    }
    this.salaryForm.patchValue({
      basicSalary: grade.basicSalary,
      hra: grade.hra,
      conveyanceAllowance: grade.conveyanceAllowance,
      medicalAllowance: grade.medicalAllowance,
      specialAllowance: grade.specialAllowance,
      providentFund: grade.providentFund,
      professionalTax: grade.professionalTax,
      incomeTax: grade.incomeTax,
    });
    this.cdr.markForCheck();
  }
  createForm() {
    this.salaryForm = this.fb.group({
      basicSalary: ['', Validators.required],
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
  loadEmployees() {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any) => {
        this.employees = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  loadSalary() {
    this.salaryService.getSalaryById(this.salaryId).subscribe({
      next: (data: any) => {
        this.salaryForm.patchValue({
          basicSalary: data.basicSalary,
          hra: data.hra,
          conveyanceAllowance: data.conveyanceAllowance,
          medicalAllowance: data.medicalAllowance,
          specialAllowance: data.specialAllowance,
          providentFund: data.providentFund,
          professionalTax: data.professionalTax,
          incomeTax: data.incomeTax,
          effectiveFrom: data.effectiveFrom,
          effectiveTo: data.effectiveTo,
          active: data.active,
          employeeId: data.employeeId,
          salaryGradeId: data.salaryGradeId ?? '',
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.errorMessage = 'Salary data load failed';
        this.cdr.markForCheck();
      },
    });
  }
  updateSalary() {
    if (this.salaryForm.invalid) {
      this.salaryForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.salaryService.updateSalary(this.salaryId, this.salaryForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Salary updated successfully.';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/salary']);
        }, 1000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Salary update failed.';
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  resetForm() {
    this.loadSalary();
    this.successMessage = '';
    this.errorMessage = '';
    this.cdr.markForCheck();
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
