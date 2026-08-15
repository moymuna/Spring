import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InterviewModel, InterviewResult } from '../../../../models/interview.model';
import { InterviewService } from '../../../../services/interview.service';
import { ApplicationService } from '../../../../services/application.service';
import { UserService } from '../../../../services/user.service';
@Component({
  selector: 'app-edit-interview',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-interview.html',
  styleUrl: './edit-interview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditInterview implements OnInit {
  id!: number;
  interview!: InterviewModel;
  results = Object.values(InterviewResult);
  applications: any[] = [];
  interviewers: any[] = [];
  errorMessage: string | null = null;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: InterviewService,
    private applicationService: ApplicationService,
    private userService: UserService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
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
    this.service.getById(this.id).subscribe((data) => {
      this.interview = data;
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.update(this.id, this.interview).subscribe({
      next: () => this.router.navigate(['/interview']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to update interview.';
        this.cdr.markForCheck();
      },
    });
  }
}
