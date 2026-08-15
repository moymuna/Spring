import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReviewStatus } from '../../../../models/performancereview.model';
import { PerformancereviewService } from '../../../../services/performancereview.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { StorageService } from '../../../../services/storage.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-performancereview',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-performancereview.html',
  styleUrl: './add-performancereview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddPerformancereview {
  performanceReviewForm!: FormGroup;
  statuses = Object.values(ReviewStatus);
  successMessage = '';
  errorMessage = '';
  reviewerName = '';
  constructor(
    private fb: FormBuilder,
    private performanceReviewService: PerformancereviewService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.performanceReviewForm = this.fb.group({
      reviewPeriodStart: ['', Validators.required],
      reviewPeriodEnd: ['', Validators.required],
      rating: ['', [Validators.required, Validators.min(0), Validators.max(5)]],
      strengths: [''],
      areasForImprovement: [''],
      comments: [''],
      status: [ReviewStatus.PENDING, Validators.required],
      employeeId: ['', Validators.required],
      reviewerId: ['', Validators.required],
    });
    this.loadCurrentReviewer();
  }
  loadCurrentReviewer() {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (emp) => {
        this.reviewerName = emp.fullName;
        this.performanceReviewForm.get('reviewerId')?.setValue(emp.id);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not resolve your employee profile as reviewer.';
        this.cdr.markForCheck();
      },
    });
  }
  savePerformanceReview() {
    if (this.performanceReviewForm.invalid) {
      this.errorMessage = 'Please fill all required fields';
      return;
    }
    this.performanceReviewService
      .createPerformanceReview(this.performanceReviewForm.value)
      .subscribe({
        next: (response) => {
          this.successMessage = 'Performance Review created successfully';
          this.performanceReviewForm.reset({
            status: ReviewStatus.PENDING,
            reviewerId: this.performanceReviewForm.get('reviewerId')?.value,
          });
          this.cdr.markForCheck();
          setTimeout(() => {
            this.router.navigate(['/performancereview']);
          }, 800);
        },
        error: (err) => {
          console.log(err);
          this.errorMessage = 'Failed to create Performance Review';
          this.cdr.markForCheck();
        },
      });
  }
}
