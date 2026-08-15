import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LeaveService } from '../../../../services/leave.service';
import { EmployeeService } from '../../../../services/employee.service';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-leave-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leave-edit.html',
  styleUrl: './leave-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeaveEdit implements OnInit {
  leaveForm!: FormGroup;
  id!: number;
  employees: any[] = [];
  leaveTypes: any[] = [];
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private leaveService: LeaveService,
    private employeeService: EmployeeService,
    private leaveTypeService: LeaveTypeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.leaveForm = this.fb.group({
      employeeId: ['', Validators.required],
      leaveTypeId: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      totalDays: ['', Validators.required],
      reason: ['', Validators.required],
      status: ['', Validators.required],
    });
    this.loadEmployees();
    this.loadLeaveTypes();
    this.loadLeave();
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
    });
  }
  loadLeave() {
    this.leaveService.getLeave(this.id).subscribe({
      next: (res) => {
        this.leaveForm.patchValue({
          employeeId: res.employeeId,
          leaveTypeId: res.leaveTypeId,
          startDate: res.startDate,
          endDate: res.endDate,
          totalDays: res.totalDays,
          reason: res.reason,
          status: res.status,
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to load leave.';
        this.cdr.markForCheck();
      },
    });
  }
  updateLeave() {
    if (this.leaveForm.invalid) {
      this.leaveForm.markAllAsTouched();
      return;
    }
    this.leaveService.updateLeave(this.id, this.leaveForm.value).subscribe({
      next: () => {
        this.successMessage = 'Leave updated successfully.';
        this.errorMessage = '';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leave']);
        }, 1000);
      },
      error: (err) => {
        console.log(err);
        this.successMessage = '';
        this.errorMessage = 'Update failed.';
        this.cdr.markForCheck();
      },
    });
  }
}
