import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LeaveBalance } from '../../../../models/leavebalance.model';
import { LeavebalanceService } from '../../../../services/leavebalance.service';
import { StorageService } from '../../../../services/storage.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-leavebalance-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './leavebalance-list.html',
  styleUrl: './leavebalance-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavebalanceList implements OnInit {
  leaveBalances: LeaveBalance[] = [];
  successMessage = '';
  errorMessage = '';
  isEmployeeOnly = false;
  constructor(
    private leaveBalanceService: LeavebalanceService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadLeaveBalances();
  }
  loadLeaveBalances() {
    if (this.storage.getRole() === 'EMPLOYEE') {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        this.errorMessage = 'Unable to load Leave Balance list.';
        this.cdr.markForCheck();
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (employee) => {
          this.leaveBalanceService.getLeaveBalancesByEmployee(employee.id!).subscribe({
            next: (res) => {
              this.leaveBalances = res;
              this.cdr.markForCheck();
            },
            error: () => {
              this.errorMessage = 'Unable to load Leave Balance list.';
              this.cdr.markForCheck();
            },
          });
        },
        error: () => {
          this.errorMessage = 'Unable to load Leave Balance list.';
          this.cdr.markForCheck();
        },
      });
      return;
    }
    this.leaveBalanceService.getAllLeaveBalances().subscribe({
      next: (res) => {
        this.leaveBalances = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to load Leave Balance list.';
        this.cdr.markForCheck();
      },
    });
  }
  deleteLeaveBalance(id: number) {
    if (!confirm('Are you sure you want to delete?')) {
      return;
    }
    this.leaveBalanceService.deleteLeaveBalance(id).subscribe({
      next: () => {
        this.successMessage = 'Leave Balance Deleted Successfully.';
        this.errorMessage = '';
        this.cdr.markForCheck();
        this.loadLeaveBalances();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Delete Failed.';
        this.cdr.markForCheck();
      },
    });
  }
}
