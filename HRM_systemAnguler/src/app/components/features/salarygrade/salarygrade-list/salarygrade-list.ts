import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { SalaryGrade } from '../../../../models/salarygrade.model';
import { SalaryGradeService } from '../../../../services/salarygrade.service';
import { StorageService } from '../../../../services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-salarygrade-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './salarygrade-list.html',
  styleUrl: './salarygrade-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SalarygradeList implements OnInit {
  grades: SalaryGrade[] = [];
  errorMessage = '';
  isAdmin = false;
  constructor(
    private salaryGradeService: SalaryGradeService,
    private storage: StorageService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.isAdmin = this.storage.getRole() === 'ADMIN';
    this.loadGrades();
  }
  loadGrades(): void {
    this.salaryGradeService.getAllGrades().subscribe({
      next: (data) => {
        this.grades = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load the salary grade list.';
        this.cdr.markForCheck();
      },
    });
  }
  editGrade(id: number): void {
    this.router.navigate(['/salarygrade/edit', id]);
  }
  deleteGrade(id: number): void {
    if (!confirm('Delete this grade? Employees already on it keep their current structure.')) {
      return;
    }
    this.salaryGradeService.deleteGrade(id).subscribe({
      next: () => {
        this.toast.success('Salary grade deleted.');
        this.loadGrades();
      },
    });
  }
}
