import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TrainingService } from '../../../../services/training.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-training',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-training.html',
  styleUrl: './add-training.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddTraining {
  trainingForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private trainingService: TrainingService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.trainingForm = this.fb.group({
      trainingTitle: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      employeeId: [''],
      departmentId: ['', Validators.required],
    });
  }
  saveTraining() {
    if (this.trainingForm.invalid) {
      this.errorMessage = 'Please fill all required fields';
      return;
    }
    const payload = {
      ...this.trainingForm.value,
      employeeId: this.trainingForm.value.employeeId || null,
    };
    this.trainingService.createTraining(payload).subscribe({
      next: (response) => {
        this.successMessage = 'Training created successfully';
        this.trainingForm.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/training']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Training creation failed';
        this.cdr.markForCheck();
      },
    });
  }
}
