import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { EducationLevel, ExperienceLevel } from '../../../models/applicant.model';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Register {
  educationOptions = Object.values(EducationLevel);
  experienceOptions = Object.values(ExperienceLevel);
  dto = {
    name: '',
    email: '',
    password: '',
    phone: '',
    address: '',
    education: [] as EducationLevel[],
    experience: [] as ExperienceLevel[],
    skills: '',
  };
  confirmPassword = '';
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  toggleEducation(level: EducationLevel, checked: boolean): void {
    if (checked) {
      if (!this.dto.education.includes(level)) {
        this.dto.education.push(level);
      }
    } else {
      this.dto.education = this.dto.education.filter((e) => e !== level);
    }
  }
  toggleExperience(level: ExperienceLevel, checked: boolean): void {
    if (checked) {
      if (!this.dto.experience.includes(level)) {
        this.dto.experience.push(level);
      }
    } else {
      this.dto.experience = this.dto.experience.filter((e) => e !== level);
    }
  }
  register(): void {
    if (this.dto.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }
    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.auth.register(this.dto).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Registration successful! Directing to login...';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message || 'Registration failed. Email might already be taken.';
        this.cdr.markForCheck();
      },
    });
  }
}
