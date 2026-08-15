import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InterviewModel, InterviewResult } from '../../../../models/interview.model';
import { InterviewService } from '../../../../services/interview.service';
import { ApplicationService } from '../../../../services/application.service';
import { UserService } from '../../../../services/user.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-interview',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-interview.html',
  styleUrl: './add-interview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddInterview {
  interview: InterviewModel = {
    applicationId: 0,
    interviewerId: 0,
    interviewDate: '',
    feedback: '',
    result: InterviewResult.PENDING,
  };
  results = Object.values(InterviewResult);
  applications: any[] = [];
  interviewers: any[] = [];
  errorMessage: string | null = null;
  constructor(
    private service: InterviewService,
    private applicationService: ApplicationService,
    private userService: UserService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.applicationService.getAll().subscribe((data) => {
      this.applications = data;
      this.cdr.markForCheck();
    });
    this.userService.getAllUsers().subscribe((data) => {
      this.interviewers = data.filter(
        (u) => u.role === 'ADMIN' || u.role === 'HR' || u.role === 'MANAGER',
      );
      this.cdr.markForCheck();
    });
  }
  save() {
    this.service.schedule(this.interview).subscribe({
      next: () => this.router.navigate(['/interview']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to schedule interview.';
        this.cdr.markForCheck();
      },
    });
  }
}
