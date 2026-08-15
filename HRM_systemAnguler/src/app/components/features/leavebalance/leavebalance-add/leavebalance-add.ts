import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../../../../services/employee.service';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { LeavebalanceService } from '../../../../services/leavebalance.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-leavebalance-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leavebalance-add.html',
  styleUrl: './leavebalance-add.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavebalanceAdd implements OnInit {
  leaveBalanceForm!: FormGroup;
  employees: any[] = [];
  leaveTypes: any[] = [];
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private leaveTypeService: LeaveTypeService,
    private leaveBalanceService: LeavebalanceService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}
  ngOnInit(): void {
    this.leaveBalanceForm = this.fb.group({
      year: [new Date().getFullYear(), Validators.required],
      totalEntitled: [0, Validators.required],
      used: [0, Validators.required],
      employeeId: ['', Validators.required],
      leaveTypeId: ['', Validators.required],
    });
    this.loadEmployees();
    this.loadLeaveTypes();
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
  saveLeaveBalance() {
    if (this.leaveBalanceForm.invalid) {
      this.leaveBalanceForm.markAllAsTouched();
      return;
    }
    this.leaveBalanceService.saveLeaveBalance(this.leaveBalanceForm.value).subscribe({
      next: (res) => {
        this.successMessage = 'Leave Balance Saved Successfully.';
        this.errorMessage = '';
        this.leaveBalanceForm.reset({
          year: new Date().getFullYear(),
          totalEntitled: 0,
          used: 0,
        });
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leavebalance']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to save Leave Balance.';
        this.successMessage = '';
        this.cdr.markForCheck();
      },
    });
  }
}
