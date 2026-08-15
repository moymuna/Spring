import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Leave } from '../../../../models/leave.model';
import { LeaveService } from '../../../../services/leave.service';
import { ToastService } from '../../../../services/toast.service';
import { StorageService } from '../../../../services/storage.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-leave-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './leave-list.html',
  styleUrl: './leave-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeaveList implements OnInit {
  leaves: Leave[] = [];
  successMessage = '';
  errorMessage = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  isEmployeeOnly = false;
  constructor(
    private leaveService: LeaveService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadLeaves();
  }
  loadLeaves() {
    if (this.isEmployeeOnly) {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        this.errorMessage = 'Failed to load leave list.';
        this.cdr.markForCheck();
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (employee) => {
          this.leaveService.getLeavesByEmployee(employee.id!).subscribe({
            next: (data) => {
              this.leaves = data;
              this.totalPages = 1;
              this.totalElements = data.length;
              this.cdr.markForCheck();
            },
            error: () => {
              this.errorMessage = 'Failed to load leave list.';
              this.cdr.markForCheck();
            },
          });
        },
        error: () => {
          this.errorMessage = 'Failed to load leave list.';
          this.cdr.markForCheck();
        },
      });
      return;
    }
    this.leaveService.getByPage(this.page, this.size).subscribe({
      next: (res) => {
        this.leaves = res.content ?? res;
        this.totalPages = res.totalPages ?? 1;
        this.totalElements = res.totalElements ?? this.leaves.length;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load leave list.';
        this.cdr.markForCheck();
      },
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadLeaves();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  deleteLeave(id: number) {
    if (!confirm('Are you sure?')) {
      return;
    }
    this.leaveService.deleteLeave(id).subscribe({
      next: () => {
        this.toast.success('Leave deleted successfully.');
        this.loadLeaves();
      },
    });
  }
  approveLeave(id: number) {
    this.leaveService.approveLeave(id).subscribe({
      next: () => {
        this.toast.success('Leave approved.');
        this.loadLeaves();
      },
    });
  }
  cancelLeave(id: number) {
    if (!confirm('Cancel this leave? Approved leave days will be returned to the balance.')) {
      return;
    }
    this.leaveService.cancelLeave(id).subscribe({
      next: () => {
        this.toast.success('Leave cancelled.');
        this.loadLeaves();
      },
    });
  }
  rejectLeave(id: number) {
    const reason = prompt('Enter rejection reason');
    if (!reason) {
      return;
    }
    this.leaveService.rejectLeave(id, reason).subscribe({
      next: () => {
        this.toast.success('Leave rejected.');
        this.loadLeaves();
      },
    });
  }
  editLeave(id: number) {
    this.router.navigate(['/leave/edit', id]);
  }
}
