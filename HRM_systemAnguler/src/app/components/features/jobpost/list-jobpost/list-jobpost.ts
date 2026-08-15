import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { JobPost } from '../../../../models/jobpost.model';
import { JobpostService } from '../../../../services/jobpost.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-jobpost',
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './list-jobpost.html',
  styleUrl: './list-jobpost.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListJobpost implements OnInit {
  jobPosts: JobPost[] = [];
  successMessage = '';
  errorMessage = '';
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private jobPostService: JobpostService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadJobPosts();
  }
  loadJobPosts(): void {
    this.jobPostService.getByPage(this.page, this.size).subscribe({
      next: (data) => {
        this.jobPosts = data.content ?? data;
        this.totalPages = data.totalPages ?? 1;
        this.totalElements = data.totalElements ?? this.jobPosts.length;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load Job Posts';
        this.cdr.markForCheck();
      },
    });
  }
  search(): void {
    if (this.keyword.trim() === '') {
      this.loadJobPosts();
      return;
    }
    this.jobPostService.search(this.keyword).subscribe((data) => {
      this.jobPosts = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number): void {
    this.page = pageNumber;
    this.loadJobPosts();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  edit(id: number): void {
    this.router.navigate(['/jobpost/edit', id]);
  }
  delete(id: number): void {
    if (confirm('Are you sure you want to delete this Job Post?')) {
      this.jobPostService.deleteJobPost(id).subscribe({
        next: () => {
          this.successMessage = 'Job Post deleted successfully';
          this.cdr.markForCheck();
          this.loadJobPosts();
        },
        error: () => {
          this.errorMessage = 'Delete failed';
          this.cdr.markForCheck();
        },
      });
    }
  }
}
