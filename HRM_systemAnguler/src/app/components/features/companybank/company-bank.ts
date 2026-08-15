import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CompanyBankService } from '../../../services/company-bank.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-company-bank',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './company-bank.html',
  styleUrl: './company-bank.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CompanyBank implements OnInit {
  bankForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  updatedAt?: string;
  isAdmin = false;
  constructor(
    private fb: FormBuilder,
    private service: CompanyBankService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {
    this.bankForm = this.fb.group({
      companyName: ['', Validators.required],
      bankName: ['', Validators.required],
      bankBranch: [''],
      accountName: ['', Validators.required],
      accountNumber: ['', Validators.required],
    });
  }
  ngOnInit() {
    this.isAdmin = this.storage.getRole() === 'ADMIN';
    if (!this.isAdmin) {
      this.bankForm.disable();
    }
    this.service.get().subscribe((account) => {
      if (account) {
        this.bankForm.patchValue(account);
        this.updatedAt = account.updatedAt;
      }
      this.cdr.markForCheck();
    });
  }
  save() {
    if (this.bankForm.invalid) {
      this.bankForm.markAllAsTouched();
      return;
    }
    this.service.save(this.bankForm.value).subscribe({
      next: (saved) => {
        this.successMessage = 'Company bank account saved.';
        this.errorMessage = '';
        this.updatedAt = saved.updatedAt;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.successMessage = '';
        this.errorMessage = err.error?.message ?? 'Failed to save company bank account.';
        this.cdr.markForCheck();
      },
    });
  }
}
