import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HolidayService } from '../../../../services/holiday.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-holiday',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-holiday.html',
  styleUrl: './add-holiday.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddHoliday {
  holidayForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private holidayService: HolidayService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.holidayForm = this.fb.group({
      name: ['', Validators.required],
      date: ['', Validators.required],
      recurringYearly: [false],
      description: [''],
    });
  }
  saveHoliday() {
    if (this.holidayForm.invalid) {
      this.errorMessage = 'Please fill all required fields';
      return;
    }
    this.holidayService.saveHoliday(this.holidayForm.value).subscribe({
      next: (response) => {
        this.successMessage = 'Holiday created successfully';
        this.holidayForm.reset({
          recurringYearly: false,
        });
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/holiday']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Holiday creation failed';
        this.cdr.markForCheck();
      },
    });
  }
}
