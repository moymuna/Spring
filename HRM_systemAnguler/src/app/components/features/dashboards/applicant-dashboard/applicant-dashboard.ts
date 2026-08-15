import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StorageService } from '../../../../services/storage.service';
import { ApplicantService } from '../../../../services/applicant-service';
import { UserService } from '../../../../services/user.service';
import { ToastService } from '../../../../services/toast.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
@Component({
  selector: 'app-applicant-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './applicant-dashboard.html',
  styleUrl: './applicant-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicantDashboard implements OnInit {
  user: any = null;
  applicant: any = null;
  photoUrl = '';
  signatureUrl = '';
  cvFileUrl = '';
  formModel = {
    name: '',
    email: '',
    phone: '',
    address: '',
    education: [] as string[],
    experience: [] as string[],
    skills: '',
    cvPath: '',
    password: 'dummyPassword123',
  };
  educationOptions = ['SSC', 'HSC', 'BACHELORS', 'MASTERS', 'PHD'];
  experienceOptions = ['FRESHER', 'JUNIOR', 'MID', 'SENIOR'];
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  activeTab = 'profile';
  accountEmail = '';
  emailSaving = false;
  passwordModel = { oldPassword: '', newPassword: '', confirmPassword: '' };
  passwordSaving = false;
  constructor(
    private storage: StorageService,
    private applicantService: ApplicantService,
    private userService: UserService,
    private toast: ToastService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const session = this.storage.getUser();
    if (session) {
      this.loadUserProfile(session.id);
    }
  }
  loadUserProfile(userId: number): void {
    this.http.get<any>(`${environment.apiUrl}user/${userId}`).subscribe({
      next: (data) => {
        this.user = data;
        this.photoUrl = data.photoPath
          ? this.storage.appendToken(`${environment.imgUrl}${data.photoPath}`)
          : 'https://placehold.co/150';
        this.signatureUrl = data.signaturePath
          ? this.storage.appendToken(`${environment.imgUrl}${data.signaturePath}`)
          : 'https://placehold.co/150x50?text=No+Signature';
        this.formModel.email = data.email;
        this.formModel.name = data.fullName;
        this.accountEmail = data.email;
        this.loadApplicantProfile(data.email);
        this.cdr.markForCheck();
      },
    });
  }
  loadApplicantProfile(email: string): void {
    this.applicantService.getAllApplicants().subscribe({
      next: (applicants: any[]) => {
        const found = applicants.find((a: any) => a.email === email);
        if (found) {
          this.applicant = found;
          this.formModel.name = found.name;
          this.formModel.phone = found.phone || '';
          this.formModel.address = found.address || '';
          this.formModel.skills = found.skills || '';
          this.formModel.cvPath = found.cvPath || '';
          this.formModel.education = found.education || [];
          this.formModel.experience = found.experience || [];
          if (found.cvPath) {
            this.cvFileUrl = this.storage.appendToken(`${environment.imgUrl}${found.cvPath}`);
          }
        }
        this.cdr.detectChanges();
      },
    });
  }
  toggleEducation(edu: string): void {
    const idx = this.formModel.education.indexOf(edu);
    if (idx > -1) {
      this.formModel.education.splice(idx, 1);
    } else {
      this.formModel.education.push(edu);
    }
    this.cdr.detectChanges();
  }
  toggleExperience(exp: string): void {
    const idx = this.formModel.experience.indexOf(exp);
    if (idx > -1) {
      this.formModel.experience.splice(idx, 1);
    } else {
      this.formModel.experience.push(exp);
    }
    this.cdr.detectChanges();
  }
  onPhotoSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      this.http.post<any>(`${environment.apiUrl}upload`, formData).subscribe({
        next: (res) => {
          this.user.photoPath = res.filePath;
          this.saveUserChanges();
        },
      });
    }
  }
  onSignatureSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      this.http.post<any>(`${environment.apiUrl}upload`, formData).subscribe({
        next: (res) => {
          this.user.signaturePath = res.filePath;
          this.saveUserChanges();
        },
      });
    }
  }
  onCvSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      this.http.post<any>(`${environment.apiUrl}upload`, formData).subscribe({
        next: (res) => {
          this.formModel.cvPath = res.filePath;
          this.cvFileUrl = this.storage.appendToken(`${environment.imgUrl}${res.filePath}`);
          this.cdr.detectChanges();
        },
      });
    }
  }
  saveUserChanges(): void {
    const updateDto = {
      fullName: this.user.fullName,
      email: this.user.email,
      role: this.user.role,
      enabled: this.user.enabled,
      accountLocked: this.user.accountLocked,
      photoPath: this.user.photoPath,
      signaturePath: this.user.signaturePath,
    };
    this.http.put<any>(`${environment.apiUrl}user/${this.user.id}`, updateDto).subscribe({
      next: (data) => {
        this.user = data;
        this.photoUrl = data.photoPath
          ? this.storage.appendToken(`${environment.imgUrl}${data.photoPath}`)
          : 'https://placehold.co/150';
        this.signatureUrl = data.signaturePath
          ? this.storage.appendToken(`${environment.imgUrl}${data.signaturePath}`)
          : 'https://placehold.co/150x50?text=No+Signature';
        this.cdr.detectChanges();
      },
    });
  }
  saveApplicantProfile(): void {
    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;
    const payload: any = {
      name: this.formModel.name,
      email: this.formModel.email,
      phone: this.formModel.phone,
      address: this.formModel.address,
      education: this.formModel.education,
      experience: this.formModel.experience,
      skills: this.formModel.skills,
      cvPath: this.formModel.cvPath,
      user: { id: this.user.id },
    };
    if (this.applicant) {
      payload.id = this.applicant.id;
      this.applicantService.updateApplicant(this.applicant.id, payload).subscribe({
        next: (data) => {
          this.loading = false;
          this.applicant = data;
          this.successMessage = 'Profile and CV saved successfully!';
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Failed to save CV details.';
          this.cdr.detectChanges();
        },
      });
    } else {
      this.applicantService.createApplicant(payload).subscribe({
        next: (data) => {
          this.loading = false;
          this.applicant = data;
          this.successMessage = 'Profile and CV created successfully!';
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Failed to create CV details.';
          this.cdr.detectChanges();
        },
      });
    }
  }
  printCV(): void {
    window.print();
  }
  updateEmail(): void {
    if (!this.accountEmail || !this.user) {
      return;
    }
    this.emailSaving = true;
    const payload: any = {
      fullName: this.user.fullName,
      email: this.accountEmail,
      role: this.user.role,
    };
    this.userService.updateUser(this.user.id, payload).subscribe({
      next: (data) => {
        this.emailSaving = false;
        this.user = data;
        this.formModel.email = data.email;
        this.toast.success('Email updated successfully.');
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.emailSaving = false;
        this.toast.error(err.error?.message || 'Failed to update email.');
        this.cdr.markForCheck();
      },
    });
  }
  changePassword(): void {
    if (!this.user) {
      return;
    }
    if (!this.passwordModel.oldPassword || !this.passwordModel.newPassword) {
      this.toast.error('Please fill in both password fields.');
      return;
    }
    if (this.passwordModel.newPassword !== this.passwordModel.confirmPassword) {
      this.toast.error('New password and confirmation do not match.');
      return;
    }
    this.passwordSaving = true;
    this.userService
      .changePassword(this.user.id, this.passwordModel.oldPassword, this.passwordModel.newPassword)
      .subscribe({
        next: () => {
          this.passwordSaving = false;
          this.passwordModel = { oldPassword: '', newPassword: '', confirmPassword: '' };
          this.toast.success('Password changed successfully.');
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.passwordSaving = false;
          this.toast.error(err.error?.message || 'Failed to change password.');
          this.cdr.markForCheck();
        },
      });
  }
}
