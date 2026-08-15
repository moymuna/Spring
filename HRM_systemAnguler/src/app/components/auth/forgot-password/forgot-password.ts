import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicHeader } from '../../public/public-header/public-header';
@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, PublicHeader],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ForgotPassword {
  email = '';
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  constructor(
    private auth: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}
  submit(): void {
    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;
    this.auth.forgotPassword({ email: this.email }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `A password reset link has been sent to ${this.email}.`;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Something went wrong. Please try again shortly.';
        this.cdr.markForCheck();
      },
    });
  }
  scrollTo(id: string): void {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  }
}
