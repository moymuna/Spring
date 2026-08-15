import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Payroll } from '../../../../models/payroll.model';
import { PayrollService } from '../../../../services/payroll.service';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { ToastService } from '../../../../services/toast.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-payroll',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './list-payroll.html',
  styleUrl: './list-payroll.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListPayroll implements OnInit {
  payrolls: Payroll[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  employees: EmployeeResponse[] = [];
  generateEmployeeId = 0;
  generateYear = new Date().getFullYear();
  generateMonth = new Date().getMonth() + 1;
  generating = false;
  constructor(
    private payrollService: PayrollService,
    private employeeService: EmployeeService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadPayrolls();
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
  }
  payPayroll(id: number) {
    if (!confirm('Mark this salary as paid? The employee will be notified of the bank transfer.')) {
      return;
    }
    this.payrollService.payPayroll(id).subscribe({
      next: () => {
        this.toast.success('Salary marked as paid.');
        this.loadPayrolls();
      },
    });
  }
  generatePayroll() {
    if (!this.generateEmployeeId) {
      this.toast.warning('Select an employee first.');
      return;
    }
    this.generating = true;
    this.payrollService
      .generatePayroll(this.generateEmployeeId, this.generateYear, this.generateMonth)
      .subscribe({
        next: () => {
          this.generating = false;
          this.toast.success('Payroll generated successfully.');
          this.cdr.markForCheck();
          this.loadPayrolls();
        },
        error: () => {
          this.generating = false;
          this.cdr.markForCheck();
        },
      });
  }
  loadPayrolls() {
    this.payrollService.getByPage(this.page, this.size).subscribe({
      next: (data) => {
        this.payrolls = data.content ?? data;
        this.totalPages = data.totalPages ?? 1;
        this.totalElements = data.totalElements ?? this.payrolls.length;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadPayrolls();
      return;
    }
    this.payrollService.search(this.keyword).subscribe((data) => {
      this.payrolls = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadPayrolls();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  deletePayroll(id: number) {
    if (confirm('Delete Payroll?')) {
      this.payrollService.deletePayroll(id).subscribe(() => {
        this.loadPayrolls();
      });
    }
  }
}
