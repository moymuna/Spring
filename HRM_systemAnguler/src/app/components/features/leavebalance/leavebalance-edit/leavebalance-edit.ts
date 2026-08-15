import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LeavebalanceService } from '../../../../services/leavebalance.service';
import { EmployeeService } from '../../../../services/employee.service';
import { LeaveTypeService } from '../../../../services/leave-type.service';
@Component({
  selector: 'app-leavebalance-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leavebalance-edit.html',
  styleUrl: './leavebalance-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavebalanceEdit implements OnInit {
  leaveBalanceForm!: FormGroup;
  id!: number;
  employees: any[] = [];
  leaveTypes: any[] = [];
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private leaveBalanceService: LeavebalanceService,
    private employeeService: EmployeeService,
    private leaveTypeService: LeaveTypeService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.leaveBalanceForm = this.fb.group({
      year: ['', Validators.required],
      totalEntitled: ['', Validators.required],
      used: ['', Validators.required],
      employeeId: ['', Validators.required],
      leaveTypeId: ['', Validators.required],
    });
    this.loadEmployees();
    this.loadLeaveTypes();
    this.loadLeaveBalance();
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
  loadLeaveBalance() {
    this.leaveBalanceService.getLeaveBalance(this.id).subscribe({
      next: (res) => {
        this.leaveBalanceForm.patchValue({
          year: res.year,
          totalEntitled: res.totalEntitled,
          used: res.used,
          employeeId: res.employeeId,
          leaveTypeId: res.leaveTypeId,
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to load Leave Balance.';
        this.cdr.markForCheck();
      },
    });
  }
  updateLeaveBalance() {
    if (this.leaveBalanceForm.invalid) {
      this.leaveBalanceForm.markAllAsTouched();
      return;
    }
    this.leaveBalanceService.updateLeaveBalance(this.id, this.leaveBalanceForm.value).subscribe({
      next: (res) => {
        this.successMessage = 'Leave Balance Updated Successfully.';
        this.errorMessage = '';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leavebalance']);
        }, 1000);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Update Failed.';
        this.successMessage = '';
        this.cdr.markForCheck();
      },
    });
  }
}
