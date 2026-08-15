import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NoticeService } from '../../../../services/notice.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-notice',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-notice.html',
  styleUrl: './add-notice.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddNotice {
  noticeForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private noticeService: NoticeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.noticeForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      publishDate: ['', Validators.required],
      officeId: ['', Validators.required],
    });
  }
  saveNotice() {
    if (this.noticeForm.invalid) {
      this.errorMessage = 'Please fill all required fields';
      return;
    }
    this.noticeService.createNotice(this.noticeForm.value).subscribe({
      next: (response) => {
        this.successMessage = 'Notice created successfully';
        this.noticeForm.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/notice']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Notice creation failed';
        this.cdr.markForCheck();
      },
    });
  }
}
