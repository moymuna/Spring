import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingService } from '../../../../services/training.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-training',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-training.html',
  styleUrl: './edit-training.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditTraining implements OnInit {
  trainingForm!: FormGroup;
  id!: number;
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private trainingService: TrainingService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.trainingForm = this.fb.group({
      trainingTitle: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      employeeId: ['', Validators.required],
      departmentId: ['', Validators.required],
    });
    this.loadTraining();
  }
  loadTraining() {
    this.trainingService.getTrainingById(this.id).subscribe((data) => {
      this.trainingForm.patchValue(data);
      this.cdr.markForCheck();
    });
  }
  updateTraining() {
    this.trainingService.updateTraining(this.id, this.trainingForm.value).subscribe(() => {
      this.toast.success('Training updated successfully');
      this.router.navigate(['/training']);
    });
  }
}
