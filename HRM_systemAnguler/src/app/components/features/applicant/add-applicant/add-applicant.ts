import { Component } from '@angular/core';
import { Applicant, EducationLevel, ExperienceLevel } from '../../../../models/applicant.model';
import { ApplicantService } from '../../../../services/applicant-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
@Component({
  selector: 'app-add-applicant',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-applicant.html',
  styleUrl: './add-applicant.css',
})
export class AddApplicant {
  applicant: Applicant = {
    name: '',
    email: '',
    phone: '',
    address: '',
    education: [],
    experience: [],
    skills: '',
    cvPath: '',
    password: '',
  };
  educationList = Object.values(EducationLevel);
  experienceList = Object.values(ExperienceLevel);
  successMessage = '';
  errorMessage = '';
  uploadingCv = false;
  constructor(
    private applicantService: ApplicantService,
    private http: HttpClient,
    private router: Router,
  ) {}
  onCvSelected(event: any) {
    const file = event.target.files[0];
    if (!file) {
      return;
    }
    const formData = new FormData();
    formData.append('file', file);
    this.uploadingCv = true;
    this.http.post<any>(`${environment.apiUrl}upload`, formData).subscribe({
      next: (res) => {
        this.applicant.cvPath = res.filePath;
        this.uploadingCv = false;
      },
      error: () => {
        this.errorMessage = 'CV upload failed';
        this.uploadingCv = false;
      },
    });
  }
  toggleEducation(education: string) {
    const index = this.applicant.education.indexOf(education as EducationLevel);
    if (index === -1) {
      this.applicant.education.push(education as EducationLevel);
    } else {
      this.applicant.education.splice(index, 1);
    }
  }
  toggleExperience(experience: string) {
    const index = this.applicant.experience.indexOf(experience as ExperienceLevel);
    if (index === -1) {
      this.applicant.experience.push(experience as ExperienceLevel);
    } else {
      this.applicant.experience.splice(index, 1);
    }
  }
  save() {
    this.applicantService.createApplicant(this.applicant).subscribe({
      next: (response) => {
        this.successMessage = 'Applicant created successfully';
        this.applicant = {
          name: '',
          email: '',
          phone: '',
          address: '',
          education: [],
          experience: [],
          skills: '',
          cvPath: '',
          password: '',
        };
        setTimeout(() => {
          this.router.navigate(['/applicant']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Applicant creation failed';
      },
    });
  }
}
