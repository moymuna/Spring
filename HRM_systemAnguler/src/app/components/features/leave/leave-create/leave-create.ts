import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { LeaveService } from '../../../../services/leave.service';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { StorageService } from '../../../../services/storage.service';
@Component({
  selector: 'app-leave-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leave-create.html',
  styleUrl: './leave-create.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeaveCreate implements OnInit {
  leaveForm!: FormGroup;
  employees: any[] = [];
  leaveTypes: any[] = [];
  successMessage = '';
  errorMessage = '';
  isEmployeeOnly = false;
  selfEmployeeName = '';
  constructor(
    private fb: FormBuilder,
    private leaveService: LeaveService,
    private employeeService: EmployeeService,
    private leaveTypeService: LeaveTypeService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}
  ngOnInit(): void {
    this.leaveForm = this.fb.group({
      employeeId: ['', Validators.required],
      leaveTypeId: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      totalDays: ['', Validators.required],
      reason: ['', Validators.required],
      status: ['PENDING'],
    });
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    if (this.isEmployeeOnly) {
      this.loadSelfEmployee();
    } else {
      this.loadEmployees();
    }
    this.loadLeaveTypes();
    this.leaveForm.get('startDate')?.valueChanges.subscribe(() => this.recalculateTotalDays());
    this.leaveForm.get('endDate')?.valueChanges.subscribe(() => this.recalculateTotalDays());
  }
  private recalculateTotalDays(): void {
    const start = this.leaveForm.get('startDate')?.value;
    const end = this.leaveForm.get('endDate')?.value;
    if (!start || !end) {
      return;
    }
    const startDate = new Date(start);
    const endDate = new Date(end);
    const diffDays =
      Math.round((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    this.leaveForm.get('totalDays')?.setValue(diffDays > 0 ? diffDays : '', { emitEvent: false });
    this.cdr.markForCheck();
  }
  loadSelfEmployee() {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (emp) => {
        this.selfEmployeeName = emp.fullName;
        this.leaveForm.get('employeeId')?.setValue(emp.id);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load your employee profile.';
        this.cdr.markForCheck();
      },
    });
  }
  loadEmployees() {
    this.employeeService.getAllEmployees().subscribe({
      next: (res) => {
        this.employees = res;
        this.cdr.markForCheck();
      },
    });
  }
  loadLeaveTypes() {
    this.leaveTypeService.getAllLeaveTypes().subscribe({
      next: (res) => {
        this.leaveTypes = res;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load leave types.';
        this.cdr.markForCheck();
      },
    });
  }
  saveLeave() {
    if (this.leaveForm.invalid) {
      this.leaveForm.markAllAsTouched();
      return;
    }
    this.leaveService.saveLeave(this.leaveForm.value).subscribe({
      next: () => {
        this.successMessage = 'Leave Applied Successfully.';
        this.errorMessage = '';
        this.leaveForm.reset();
        this.leaveForm.patchValue({
          status: 'PENDING',
        });
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leave']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.successMessage = '';
        this.errorMessage = 'Unable to save leave.';
        this.cdr.markForCheck();
      },
    });
  }
}
