import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PerformanceReview } from '../../../../models/performancereview.model';
import { PerformancereviewService } from '../../../../services/performancereview.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-performancereview',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-performancereview.html',
  styleUrl: './list-performancereview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListPerformancereview implements OnInit {
  reviews: PerformanceReview[] = [];
  successMessage = '';
  constructor(
    private performanceReviewService: PerformancereviewService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.loadReviews();
  }
  loadReviews() {
    this.performanceReviewService.getAllPerformanceReviews().subscribe({
      next: (data) => {
        this.reviews = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  deleteReview(id: number) {
    if (confirm('Are you sure you want to delete?')) {
      this.performanceReviewService.deletePerformanceReview(id).subscribe({
        next: () => {
          this.successMessage = 'Performance Review deleted successfully';
          this.cdr.markForCheck();
          this.loadReviews();
        },
      });
    }
  }
}
