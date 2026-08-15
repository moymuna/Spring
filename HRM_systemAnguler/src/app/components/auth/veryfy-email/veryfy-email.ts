import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
type VerifyState = 'loading' | 'success' | 'error' | 'no-token';
@Component({
  selector: 'app-veryfy-email',
  imports: [CommonModule, FormsModule],
  templateUrl: './veryfy-email.html',
  styleUrl: './veryfy-email.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VeryfyEmail {
  state: VerifyState = 'loading';
  errorMessage = '';
  countdown = 5;
  private timer: any;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.state = 'no-token';
      return;
    }
    this.auth.verifyEmail(token).subscribe({
      next: () => {
        this.state = 'success';
        this.startCountdown();
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.state = 'error';
        this.errorMessage =
          err.status === 400 || err.status === 409
            ? (err.error ?? 'This verification link is invalid or has already been used.')
            : 'Something went wrong. Please request a new verification email.';
        this.cdr.markForCheck();
      },
    });
  }
  private startCountdown(): void {
    this.timer = setInterval(() => {
      this.countdown--;
      this.cdr.markForCheck();
      if (this.countdown <= 0) {
        clearInterval(this.timer);
        this.router.navigate(['/login']);
      }
    }, 1000);
  }
  goToLogin(): void {
    clearInterval(this.timer);
    this.router.navigate(['/login']);
  }
  ngOnDestroy(): void {
    clearInterval(this.timer);
  }
}
