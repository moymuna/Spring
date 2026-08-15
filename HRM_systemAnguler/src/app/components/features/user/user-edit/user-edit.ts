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
import { ActivatedRoute, Router } from '@angular/router';
import { Role } from '../../../../models/user.model';
@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserEdit implements OnInit {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  userId!: number;
  loading = false;
  submitted = false;
  errorMessage = '';
  successMessage = '';
  roles = Object.values(Role);
  userForm = this.fb.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    role: [Role.EMPLOYEE, Validators.required],
    enabled: [true],
    accountLocked: [false],
  });
  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadUser();
  }
  loadUser() {
    this.loading = true;
    this.userService.getUserById(this.userId).subscribe({
      next: (user) => {
        this.userForm.patchValue({
          fullName: user.fullName,
          email: user.email,
          role: user.role,
          enabled: user.enabled,
          accountLocked: user.accountLocked,
        });
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.errorMessage = 'Failed to load user.';
        this.cdr.markForCheck();
      },
    });
  }
  updateUser() {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.userService.updateUser(this.userId, this.userForm.value as any).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'User Updated Successfully.';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/user']);
        }, 1000);
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.errorMessage = err.error?.message ?? 'Failed to update user.';
        this.cdr.markForCheck();
      },
    });
  }
  goBack() {
    this.router.navigate(['/user']);
  }
  get f() {
    return this.userForm.controls;
  }
}
