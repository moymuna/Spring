import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../../services/user.service';
import { Router } from '@angular/router';
import { Role } from '../../../../models/user.model';
@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-create.html',
  styleUrl: './user-create.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserCreate {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  roles = Object.values(Role);
  loading = false;
  errorMessage = '';
  successMessage = '';
  userForm = this.fb.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: [Role.EMPLOYEE, Validators.required],
    enabled: [true],
    accountLocked: [false],
  });
  saveUser() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.userService.createUser(this.userForm.value as any).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'User Created Successfully.';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/user']);
        }, 1000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Failed to create user.';
        this.cdr.markForCheck();
      },
    });
  }
  resetForm() {
    this.userForm.reset({
      enabled: true,
      accountLocked: false,
      role: Role.EMPLOYEE,
    });
  }
}
