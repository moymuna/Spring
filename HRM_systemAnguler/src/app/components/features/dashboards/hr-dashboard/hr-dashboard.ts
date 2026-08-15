import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EmployeeService } from '../../../../services/employee.service';
import { AttendanceService } from '../../../../services/attendance.service';
import { LeaveService } from '../../../../services/leave.service';
import { LeavebalanceService } from '../../../../services/leavebalance.service';
import { PayrollService } from '../../../../services/payroll.service';
@Component({
  selector: 'app-hr-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './hr-dashboard.html',
  styleUrl: './hr-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HrDashboard implements OnInit {
  stats = [
    {
      label: 'Employees Active',
      count: 0,
      icon: 'bi-person-check-fill',
      color: 'var(--color-accent)',
    },
    {
      label: 'Today Attendance',
      count: 0,
      icon: 'bi-clock-history',
      color: 'var(--color-success)',
    },
    {
      label: 'Open Leave Requests',
      count: 0,
      icon: 'bi-envelope-paper-fill',
      color: 'var(--color-warning)',
    },
  ];
  recentLeaves: any[] = [];
  recentAttendance: any[] = [];
  leaveUtilization: any[] = [];
  payrollTrend: {
    month: number;
    total: number;
  }[] = [];
  readonly monthNames = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'Jun',
    'Jul',
    'Aug',
    'Sep',
    'Oct',
    'Nov',
    'Dec',
  ];
  constructor(
    private employeeService: EmployeeService,
    private attendanceService: AttendanceService,
    private leaveService: LeaveService,
    private leaveBalanceService: LeavebalanceService,
    private payrollService: PayrollService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadHRData();
    this.loadAnalytics();
  }
  loadAnalytics(): void {
    const year = new Date().getFullYear();
    this.leaveBalanceService.getUtilizationByYear(year).subscribe({
      next: (data) => {
        this.leaveUtilization = data;
        this.cdr.detectChanges();
      },
      error: () => {},
    });
    this.payrollService.getMonthlyCostTrend(year).subscribe({
      next: (data) => {
        this.payrollTrend = Object.keys(data)
          .map((m) => ({ month: Number(m), total: data[Number(m)] }))
          .sort((a, b) => a.month - b.month);
        this.cdr.detectChanges();
      },
      error: () => {},
    });
  }
  loadHRData(): void {
    this.employeeService.getActiveEmployeeCount().subscribe({
      next: (count: number) => {
        this.stats[0].count = count || 0;
        this.cdr.detectChanges();
      },
    });
    this.attendanceService.getTodayAttendanceCount().subscribe({
      next: (count: number) => {
        this.stats[1].count = count || 0;
        this.cdr.detectChanges();
      },
    });
    this.attendanceService.getAllAttendance().subscribe({
      next: (data: any) => {
        this.recentAttendance = data ? data.slice(0, 5) : [];
        this.cdr.detectChanges();
      },
    });
    this.leaveService.getAllLeaves().subscribe({
      next: (data: any) => {
        const pending =
          data?.filter((l: any) => l.status === 'PENDING' || l.status === 'Pending') || [];
        this.stats[2].count = pending.length;
        this.recentLeaves = pending.slice(0, 5);
        this.cdr.detectChanges();
      },
    });
  }
}
