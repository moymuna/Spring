import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Department } from '../../../../models/employee.model';
import { JobPost, JobStatus } from '../../../../models/jobpost.model';
import { JobpostService } from '../../../../services/jobpost.service';
import { DepartmentService } from '../../../../services/department.service';
import { Router } from '@angular/router';
import { DepartmentModel } from '../../../../models/department.model';
@Component({
  selector: 'app-add-jobpost',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-jobpost.html',
  styleUrl: './add-jobpost.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddJobpost implements OnInit {
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
    private jobPostService: JobpostService,
    private departmentService: DepartmentService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadDepartments();
  }
  loadDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (data) => {
        this.departments = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  save(): void {
    this.jobPostService.saveJobPost(this.jobPost).subscribe({
      next: () => {
        this.successMessage = 'Job Post Added Successfully';
        this.errorMessage = '';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/jobpost']);
        }, 1000);
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Failed to Save Job Post';
        this.cdr.markForCheck();
      },
    });
  }
}
