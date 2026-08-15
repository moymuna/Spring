import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApplicationModel, ApplicationStatus } from '../../../../models/application.model';
import { ApplicationService } from '../../../../services/application.service';
import { ApplicantService } from '../../../../services/applicant-service';
import { JobpostService } from '../../../../services/jobpost.service';
import { ToastService } from '../../../../services/toast.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-application',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-application.html',
  styleUrl: './add-application.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddApplication implements OnInit {
  application: ApplicationModel = {
    applicantId: 0,
    jobPostId: 0,
    applyDate: '',
    status: ApplicationStatus.APPLIED,
  };
  statuses = Object.values(ApplicationStatus);
  applicants: any[] = [];
  jobPosts: any[] = [];
  constructor(
    private service: ApplicationService,
    private applicantService: ApplicantService,
    private jobPostService: JobpostService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}
  ngOnInit() {
    this.applicantService.getAllApplicants().subscribe((data) => {
      this.applicants = data;
      this.cdr.markForCheck();
    });
    this.jobPostService.getAllJobPosts().subscribe((data) => {
      this.jobPosts = data;
      this.cdr.markForCheck();
    });
  }
  save() {
    this.service.applyJob(this.application).subscribe({
      next: (res) => {
        this.toast.success('Application submitted successfully');
        setTimeout(() => {
          this.router.navigate(['/application']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
