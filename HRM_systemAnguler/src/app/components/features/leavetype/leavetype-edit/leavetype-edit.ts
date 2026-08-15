import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-leavetype-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leavetype-edit.html',
  styleUrl: './leavetype-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavetypeEdit implements OnInit {
  leaveTypeForm!: FormGroup;
  id!: number;
  successMessage = '';
  errorMessage = '';
  leaveTypes = [
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
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.leaveTypeForm = this.fb.group({
      name: ['', Validators.required],
      maxDaysPerYear: ['', Validators.required],
      maxCarryForwardDays: [0],
      paid: [true, Validators.required],
      description: [''],
    });
    this.loadLeaveType();
  }
  loadLeaveType() {
    this.leaveTypeService.getLeaveTypeById(this.id).subscribe({
      next: (res) => {
        this.leaveTypeForm.patchValue({
          name: res.name,
          maxDaysPerYear: res.maxDaysPerYear,
          maxCarryForwardDays: res.maxCarryForwardDays ?? 0,
          paid: res.paid,
          description: res.description,
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to load Leave Type.';
        this.cdr.markForCheck();
      },
    });
  }
  updateLeaveType() {
    if (this.leaveTypeForm.invalid) {
      this.leaveTypeForm.markAllAsTouched();
      return;
    }
    this.leaveTypeService.updateLeaveType(this.id, this.leaveTypeForm.value).subscribe({
      next: () => {
        this.successMessage = 'Leave Type Updated Successfully.';
        this.errorMessage = '';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/leavetype']);
        }, 1000);
      },
      error: (err) => {
        console.log(err);
        this.successMessage = '';
        this.errorMessage = 'Update Failed.';
        this.cdr.markForCheck();
      },
    });
  }
}
