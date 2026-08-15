import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HolidayService } from '../../../../services/holiday.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-holiday',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-holiday.html',
  styleUrl: './edit-holiday.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditHoliday implements OnInit {
  holidayForm!: FormGroup;
  id!: number;
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private holidayService: HolidayService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.holidayForm = this.fb.group({
      name: ['', Validators.required],
      date: ['', Validators.required],
      recurringYearly: [false],
      description: [''],
    });
    this.loadHoliday();
  }
  loadHoliday() {
    this.holidayService.getHolidayById(this.id).subscribe((data) => {
      this.holidayForm.patchValue(data);
      this.cdr.markForCheck();
    });
  }
  updateHoliday() {
    this.holidayService.updateHoliday(this.id, this.holidayForm.value).subscribe(() => {
      this.toast.success('Holiday updated successfully');
      this.router.navigate(['/holiday']);
    });
  }
}
