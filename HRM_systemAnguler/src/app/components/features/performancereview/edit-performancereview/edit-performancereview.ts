import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReviewStatus } from '../../../../models/performancereview.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PerformancereviewService } from '../../../../services/performancereview.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-performancereview',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-performancereview.html',
  styleUrl: './edit-performancereview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPerformancereview implements OnInit {
  form!: FormGroup;
  id!: number;
  statuses = Object.values(ReviewStatus);
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private service: PerformancereviewService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.form = this.fb.group({
      reviewPeriodStart: ['', Validators.required],
      reviewPeriodEnd: ['', Validators.required],
      rating: [''],
      strengths: [''],
      areasForImprovement: [''],
      comments: [''],
      status: ['', Validators.required],
      employeeId: ['', Validators.required],
      reviewerId: ['', Validators.required],
    });
    this.loadData();
  }
  loadData() {
    this.service.getPerformanceReviewById(this.id).subscribe((data) => {
      this.form.patchValue(data);
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.updatePerformanceReview(this.id, this.form.value).subscribe(() => {
      this.toast.success('Updated Successfully');
      this.router.navigate(['/performancereview']);
    });
  }
}
