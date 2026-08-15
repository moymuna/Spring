import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Applicant } from '../../../../models/applicant.model';
import { ApplicantService } from '../../../../services/applicant-service';
import { ToastService } from '../../../../services/toast.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-applicant',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './list-applicant.html',
  styleUrl: './list-applicant.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListApplicant implements OnInit {
  applicants: Applicant[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private applicantService: ApplicantService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.loadApplicants();
  }
  loadApplicants() {
    this.applicantService.getByPage(this.page, this.size).subscribe({
      next: (data) => {
        this.applicants = data.content ?? data;
        this.totalPages = data.totalPages ?? 1;
        this.totalElements = data.totalElements ?? this.applicants.length;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadApplicants();
      return;
    }
    this.applicantService.search(this.keyword).subscribe((data) => {
      this.applicants = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadApplicants();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  deleteApplicant(id: number) {
    if (confirm('Are you sure you want to delete this applicant?')) {
      this.applicantService.deleteApplicant(id).subscribe(() => {
        this.toast.success('Applicant deleted successfully');
        this.loadApplicants();
      });
    }
  }
}
