import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-leavetype-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leavetype-add.html',
  styleUrl: './leavetype-add.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavetypeAdd {
  leaveTypeForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  leaveTypeOptions = [
    'CASUAL_LEAVE',
    'SICK_LEAVE',
    'EARNED_LEAVE',
    'MATERNITY_LEAVE',
    'PATERNITY_LEAVE',
    'UNPAID_LEAVE',
    'COMPENSATORY_LEAVE',
    'STUDY_LEAVE',
  ];
  constructor(
    private fb: FormBuilder,
    private leaveTypeService: LeaveTypeService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {
    this.leaveTypeForm = this.fb.group({
      name: ['', Validators.required],
      maxDaysPerYear: ['', Validators.required],
      maxCarryForwardDays: [0],
      paid: [true, Validators.required],
      description: [''],
    });
  }
  saveLeaveType() {
    if (this.leaveTypeForm.invalid) {
      this.leaveTypeForm.markAllAsTouched();
      return;
    }
    this.leaveTypeService.saveLeaveType(this.leaveTypeForm.value).subscribe({
      next: () => {
        this.successMessage = 'Leave Type saved successfully.';
        this.errorMessage = '';
        this.leaveTypeForm.reset();
        this.leaveTypeForm.patchValue({
          paid: true,
        });
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leavetype']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.successMessage = '';
        this.errorMessage = 'Failed to save Leave Type.';
        this.cdr.markForCheck();
      },
    });
  }
}
