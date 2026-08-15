import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PublicHeader } from '../../public/public-header/public-header';
import { LoginRequest } from '../../../models/auth.model';
import { AuthService } from '../../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule, PublicHeader, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Login {
  dto: LoginRequest = { email: '', password: '' };
  showPassword = false;
  loading = false;
  errorMessage: string | null = null;
  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  login(): void {
    this.loading = true;
    this.errorMessage = null;
    this.auth.login(this.dto).subscribe({
      next: () => {
        this.loading = false;
        this.cdr.markForCheck();
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err.status === 401
            ? 'Invalid email or password.'
            : err.status === 403
              ? 'Your account is not verified or has been disabled.'
              : 'Something went wrong. Please try again.';
        this.cdr.markForCheck();
      },
    });
  }
}
