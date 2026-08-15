import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Training } from '../../../../models/traning.model';
import { TrainingService } from '../../../../services/training.service';
import { StorageService } from '../../../../services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-training',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-training.html',
  styleUrl: './list-training.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListTraining implements OnInit {
  trainings: Training[] = [];
  successMessage = '';
  isEmployeeOnly = false;
  selfEmployeeId: number | null = null;
  constructor(
    private trainingService: TrainingService,
    private storage: StorageService,
    private toast: ToastService,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    if (this.isEmployeeOnly) {
      this.loadSelfEmployee();
    }
    this.loadTraining();
  }
  loadSelfEmployee() {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (emp) => {
        this.selfEmployeeId = emp.id!;
        this.cdr.markForCheck();
      },
    });
  }
  loadTraining() {
    this.trainingService.getAllTraining().subscribe({
      next: (data) => {
        this.trainings = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  deleteTraining(id: number) {
    if (confirm('Are you sure you want to delete this training?')) {
      this.trainingService.deleteTraining(id).subscribe({
        next: () => {
          this.successMessage = 'Training deleted successfully';
          this.cdr.markForCheck();
          this.loadTraining();
        },
      });
    }
  }
  approveTraining(id: number) {
    this.trainingService.approveTraining(id).subscribe({
      next: () => {
        this.toast.success('Training application approved.');
        this.loadTraining();
      },
      error: () => {
        this.toast.error('Failed to approve training application.');
      },
    });
  }
  rejectTraining(id: number) {
    const reason = prompt('Enter rejection reason');
    if (!reason) {
      return;
    }
    this.trainingService.rejectTraining(id, reason).subscribe({
      next: () => {
        this.toast.success('Training application rejected.');
        this.loadTraining();
      },
      error: () => {
        this.toast.error('Failed to reject training application.');
      },
    });
  }
  applyForTraining(id: number) {
    if (!this.selfEmployeeId) {
      this.toast.error('Could not resolve your employee profile.');
      return;
    }
    this.trainingService.applyForTraining(id, this.selfEmployeeId).subscribe({
      next: () => {
        this.toast.success('Applied for training successfully.');
        this.loadTraining();
      },
      error: () => {
        this.toast.error('Failed to apply for training.');
      },
    });
  }
}
