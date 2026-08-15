import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DepartmentModel } from '../../../../models/department.model';
import { JobPost, JobStatus } from '../../../../models/jobpost.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JobpostService } from '../../../../services/jobpost.service';
import { DepartmentService } from '../../../../services/department.service';
@Component({
  selector: 'app-edit-jobpost',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-jobpost.html',
  styleUrl: './edit-jobpost.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditJobpost implements OnInit {
  id!: number;
  departments: DepartmentModel[] = [];
  statusList = Object.values(JobStatus);
  successMessage = '';
  errorMessage = '';
  jobPost: JobPost = {
    title: '',
    description: '',
    requirements: '',
    location: '',
    minSalary: 0,
    maxSalary: 0,
    postedDate: '',
    deadline: '',
    status: JobStatus.OPEN,
    departmentId: 0,
  };
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobPostService: JobpostService,
    private departmentService: DepartmentService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDepartments();
    this.loadJobPost();
  }
  loadDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (data) => {
        this.departments = data;
        this.cdr.markForCheck();
      },
    });
  }
  loadJobPost(): void {
    this.jobPostService.getJobPostById(this.id).subscribe({
      next: (data) => {
        this.jobPost = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to Load Job Post';
        this.cdr.markForCheck();
      },
    });
  }
  update(): void {
    this.jobPostService.updateJobPost(this.id, this.jobPost).subscribe({
      next: () => {
        this.successMessage = 'Job Post Updated Successfully';
        this.errorMessage = '';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/jobpost']);
        }, 1000);
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Update Failed';
        this.cdr.markForCheck();
      },
    });
  }
}
