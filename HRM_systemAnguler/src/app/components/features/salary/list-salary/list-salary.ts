import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Salary } from '../../../../models/salary.model';
import { SalaryService } from '../../../../services/salary.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-salary',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-salary.html',
  styleUrl: './list-salary.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListSalary implements OnInit {
  salaries: Salary[] = [];
  successMessage = '';
  errorMessage = '';
  loading = false;
  constructor(
    private salaryService: SalaryService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadSalaries();
  }
  loadSalaries(): void {
    this.loading = true;
    this.salaryService.getAllSalary().subscribe({
      next: (data) => {
        this.salaries = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Failed to load salary data.';
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  editSalary(id: number): void {
    this.router.navigate(['/salary/edit', id]);
  }
  deleteSalary(id: number): void {
    if (confirm('Are you sure you want to delete this salary?')) {
      this.salaryService.deleteSalary(id).subscribe({
        next: () => {
          this.successMessage = 'Salary deleted successfully.';
          this.cdr.markForCheck();
          this.loadSalaries();
        },
        error: (err) => {
          console.log(err);
          this.errorMessage = 'Salary delete failed.';
          this.cdr.markForCheck();
        },
      });
    }
  }
}
