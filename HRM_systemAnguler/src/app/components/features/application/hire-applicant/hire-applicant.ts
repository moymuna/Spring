import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ApplicationService } from '../../../../services/application.service';
import { EmployeeService } from '../../../../services/employee.service';
import { DepartmentService } from '../../../../services/department.service';
import { DesignationService } from '../../../../services/designation.service';
import { OfficeService } from '../../../../services/office.service';
import { ToastService } from '../../../../services/toast.service';
/**
 * Turns a hired applicant into an employee. Their name, email and address carry
 * over from the application, so only the employment terms are asked for here.
 */
@Component({
  selector: 'app-hire-applicant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './hire-applicant.html',
  styleUrl: './hire-applicant.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HireApplicant implements OnInit {
  hireForm!: FormGroup;
  applicationId!: number;
  application: any = null;
  departments: any[] = [];
  designations: any[] = [];
  offices: any[] = [];
  managers: any[] = [];
  employmentTypes = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN', 'FREELANCE'];
  loading = false;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private applicationService: ApplicationService,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private designationService: DesignationService,
    private officeService: OfficeService,
    private toast: ToastService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.hireForm = this.fb.group({
      joiningDate: [new Date().toISOString().substring(0, 10), Validators.required],
      employmentType: ['FULL_TIME', Validators.required],
      departmentId: [''],
      designationId: [''],
      officeId: [''],
      managerId: [''],
      employeeCode: [''],
      contractNo: [''],
    });
    this.applicationId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadApplication();
    this.loadLookups();
  }
  loadApplication(): void {
    this.applicationService.getById(this.applicationId).subscribe({
      next: (data) => {
        this.application = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load the application.';
        this.cdr.markForCheck();
      },
    });
  }
  loadLookups(): void {
    this.departmentService.getAll().subscribe({
      next: (data: any) => {
        this.departments = data;
        this.cdr.markForCheck();
      },
    });
    this.designationService.getAll().subscribe({
      next: (data: any) => {
        this.designations = data;
        this.cdr.markForCheck();
      },
    });
    this.officeService.getAllOffice().subscribe({
      next: (data: any) => {
        this.offices = data;
        this.cdr.markForCheck();
      },
    });
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any) => {
        this.managers = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.managers = [];
        this.cdr.markForCheck();
      },
    });
  }
  /** Blank optional selects must go over as null, not ''. */
  private buildPayload() {
    const v = this.hireForm.value;
    return {
      joiningDate: v.joiningDate,
      employmentType: v.employmentType,
      departmentId: v.departmentId || null,
      designationId: v.designationId || null,
      officeId: v.officeId || null,
      managerId: v.managerId || null,
      employeeCode: v.employeeCode || null,
      contractNo: v.contractNo || null,
    };
  }
  hire(): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.hireForm.invalid) {
      this.hireForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.employeeService.hireApplicant(this.applicationId, this.buildPayload()).subscribe({
      next: (employee: any) => {
        this.loading = false;
        this.successMessage = `Hired successfully as ${employee.employeeCode}.`;
        this.toast.success('Applicant hired.');
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/application']), 1000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Unable to hire this applicant.';
        this.cdr.markForCheck();
      },
    });
  }
  get f() {
    return this.hireForm.controls;
  }
}
