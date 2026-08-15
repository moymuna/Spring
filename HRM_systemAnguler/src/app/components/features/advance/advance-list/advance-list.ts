import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Advance } from '../../../../models/advance.model';
import { AdvanceService } from '../../../../services/advance.service';
import { EmployeeService } from '../../../../services/employee.service';
import { StorageService } from '../../../../services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-advance-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './advance-list.html',
  styleUrl: './advance-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdvanceList implements OnInit {
  advances: Advance[] = [];
  errorMessage = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  isEmployeeOnly = false;
  canDecide = false;
  canDisburse = false;
  constructor(
    private advanceService: AdvanceService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const role = this.storage.getRole();
    this.isEmployeeOnly = role === 'EMPLOYEE';
    this.canDecide = role === 'ADMIN' || role === 'HR' || role === 'MANAGER';
    this.canDisburse = role === 'ADMIN' || role === 'HR';
    this.loadAdvances();
  }
  loadAdvances(): void {
    if (this.isEmployeeOnly) {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        this.errorMessage = 'Failed to load the advance list.';
        this.cdr.markForCheck();
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (employee) => {
          this.advanceService.getAdvancesByEmployee(employee.id!).subscribe({
            next: (data) => {
              this.advances = data;
              this.totalPages = 1;
              this.totalElements = data.length;
              this.cdr.markForCheck();
            },
            error: () => {
              this.errorMessage = 'Failed to load the advance list.';
              this.cdr.markForCheck();
            },
          });
        },
        error: () => {
          this.errorMessage = 'Failed to load the advance list.';
          this.cdr.markForCheck();
        },
      });
      return;
    }
    this.advanceService.getByPage(this.page, this.size).subscribe({
      next: (res) => {
        this.advances = res.content ?? res;
        this.totalPages = res.totalPages ?? 1;
        this.totalElements = res.totalElements ?? this.advances.length;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load the advance list.';
        this.cdr.markForCheck();
      },
    });
  }
  changePage(pageNumber: number): void {
    this.page = pageNumber;
    this.loadAdvances();
  }
  getPages(): number[] {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  editAdvance(id: number): void {
    this.router.navigate(['/advance/edit', id]);
  }
  deleteAdvance(id: number): void {
    if (!confirm('Are you sure?')) {
      return;
    }
    this.advanceService.deleteAdvance(id).subscribe({
      next: () => {
        this.toast.success('Advance request deleted.');
        this.loadAdvances();
      },
    });
  }
  approveAdvance(id: number): void {
    this.advanceService.approveAdvance(id).subscribe({
      next: () => {
        this.toast.success('Advance approved.');
        this.loadAdvances();
      },
    });
  }
  rejectAdvance(id: number): void {
    const reason = prompt('Enter rejection reason');
    if (!reason) {
      return;
    }
    this.advanceService.rejectAdvance(id, reason).subscribe({
      next: () => {
        this.toast.success('Advance rejected.');
        this.loadAdvances();
      },
    });
  }
  markAsPaid(id: number): void {
    this.advanceService.markAsPaid(id).subscribe({
      next: () => {
        this.toast.success('Advance marked as disbursed.');
        this.loadAdvances();
      },
    });
  }
  recordRecovery(advance: Advance): void {
    const input = prompt(
      `Recovery amount (outstanding ${advance.outstandingAmount})`,
      String(advance.monthlyDeduction ?? ''),
    );
    if (!input) {
      return;
    }
    const amount = Number(input);
    if (!amount || amount <= 0) {
      this.toast.error('Enter a recovery amount greater than zero.');
      return;
    }
    this.advanceService.recordRecovery(advance.id!, amount).subscribe({
      next: () => {
        this.toast.success('Recovery recorded.');
        this.loadAdvances();
      },
    });
  }
}
