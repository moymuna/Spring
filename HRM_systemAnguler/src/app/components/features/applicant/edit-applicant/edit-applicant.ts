import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EducationLevel, ExperienceLevel } from '../../../../models/applicant.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicantService } from '../../../../services/applicant-service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-applicant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-applicant.html',
  styleUrl: './edit-applicant.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditApplicant implements OnInit {
  applicantForm!: FormGroup;
  id!: number;
  errorMessage = '';
  educationLevels = Object.values(EducationLevel);
  experienceLevels = Object.values(ExperienceLevel);
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private applicantService: ApplicantService,
    private toast: ToastService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.applicantForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      address: [''],
      education: [[]],
      experience: [[]],
      skills: [''],
      cvPath: [''],
      password: [''],
    });
    this.loadApplicant();
  }
  loadApplicant() {
    this.applicantService.getApplicantById(this.id).subscribe({
      next: (data) => {
        this.applicantForm.patchValue(data);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load this applicant.';
        this.cdr.markForCheck();
      },
    });
  }
  updateApplicant() {
    this.errorMessage = '';
    if (this.applicantForm.invalid) {
      this.applicantForm.markAllAsTouched();
      this.errorMessage = 'Please fill all required fields correctly.';
      this.cdr.markForCheck();
      return;
    }
    this.applicantService.updateApplicant(this.id, this.applicantForm.value).subscribe({
      next: () => {
        this.toast.success('Applicant updated successfully');
        this.router.navigate(['/applicant']);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to update applicant.';
        this.toast.error(this.errorMessage);
        this.cdr.markForCheck();
      },
    });
  }
}
