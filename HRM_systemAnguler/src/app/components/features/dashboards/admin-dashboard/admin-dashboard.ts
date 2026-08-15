import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DashboardService, AdminDashboard as AdminDashboardData } from '../../../../services/dashboard.service';
import { NoticeService } from '../../../../services/notice.service';
import { AuditLogService } from '../../../../services/audit-log.service';
import { AreaChart, AreaPoint } from '../../../shared/charts/area-chart';
import { DonutChart, DonutSegment } from '../../../shared/charts/donut-chart';
const MONTH_LABELS = [
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
/** Fixed colours so a status keeps the same colour between renders. */
const ATTENDANCE_COLORS: Record<string, string> = {
  PRESENT: '#16a34a',
  WORK_FROM_HOME: '#06b6d4',
  HALF_DAY: '#f59e0b',
  ON_LEAVE: '#8b5cf6',
  HOLIDAY: '#64748b',
  WEEK_OFF: '#94a3b8',
  ABSENT: '#ef4444',
};
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, AreaChart, DonutChart],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminDashboard implements OnInit {
  data: AdminDashboardData | null = null;
  loading = true;
  errorMessage = '';
  year = new Date().getFullYear();
  years: number[] = [];
  headcountTrend: AreaPoint[] = [];
  departmentSegments: DonutSegment[] = [];
  attendanceSegments: DonutSegment[] = [];
  recentNotices: any[] = [];
  recentActivities: { text: string; time: string; icon: string; type: string }[] = [];
  quickActions = [
    { label: 'Add Employee', route: '/employee/add', icon: 'bi-person-plus', tone: 'blue' },
    { label: 'Add Department', route: '/department/add', icon: 'bi-building-add', tone: 'green' },
    { label: 'Add Designation', route: '/designation/add', icon: 'bi-briefcase', tone: 'purple' },
    { label: 'Mark Attendance', route: '/attendance/add', icon: 'bi-calendar-check', tone: 'amber' },
    { label: 'Create Notice', route: '/notice/add', icon: 'bi-megaphone', tone: 'cyan' },
    { label: 'Run Payroll', route: '/payroll', icon: 'bi-calculator', tone: 'red' },
  ];
  constructor(
    private dashboardService: DashboardService,
    private noticeService: NoticeService,
    private auditLogService: AuditLogService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const current = new Date().getFullYear();
    this.years = [current, current - 1, current - 2];
    this.loadDashboard();
    this.loadNotices();
    this.loadRecentActivities();
  }
  loadDashboard(): void {
    this.loading = true;
    this.dashboardService.getAdminDashboard(this.year).subscribe({
      next: (data) => {
        this.data = data;
        this.headcountTrend = MONTH_LABELS.map((label, i) => ({
          label,
          value: data.headcountTrend?.[String(i + 1)] ?? 0,
        }));
        this.departmentSegments = Object.entries(data.headcountByDepartment ?? {}).map(
          ([label, value]) => ({ label, value }),
        );
        this.attendanceSegments = Object.entries(data.attendanceToday ?? {}).map(
          ([label, value]) => ({
            label: this.prettify(label),
            value,
            color: ATTENDANCE_COLORS[label],
          }),
        );
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load dashboard data.';
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  onYearChange(): void {
    this.loadDashboard();
  }
  get activePercent(): number {
    if (!this.data?.totalEmployees) {
      return 0;
    }
    return Math.round((this.data.activeEmployees / this.data.totalEmployees) * 1000) / 10;
  }
  get pendingApprovals(): number {
    return (this.data?.pendingLeaveApprovals ?? 0) + (this.data?.pendingAdvanceApprovals ?? 0);
  }
  private prettify(value: string): string {
    return value
      .toLowerCase()
      .split('_')
      .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
      .join(' ');
  }
  loadRecentActivities(): void {
    this.auditLogService.getLatest().subscribe({
      next: (data) => {
        this.recentActivities = data.map((entry) => ({
          text: `${entry.entityType} ${entry.action.toLowerCase()} by ${entry.actorEmail}${entry.details ? ' — ' + entry.details : ''}`,
          time: new Date(entry.timestamp).toLocaleString(),
          icon: this.iconFor(entry.action),
          type: this.typeFor(entry.action),
        }));
        this.cdr.markForCheck();
      },
      error: () => {},
    });
  }
  private iconFor(action: string): string {
    switch (action) {
      case 'CREATE':
        return 'bi-plus-circle-fill';
      case 'UPDATE':
        return 'bi-pencil-fill';
      case 'DELETE':
        return 'bi-trash-fill';
      case 'APPROVE':
        return 'bi-check-circle-fill';
      case 'REJECT':
        return 'bi-x-circle-fill';
      case 'LOGIN_FAILED':
        return 'bi-exclamation-triangle-fill';
      case 'ACCOUNT_LOCKED':
        return 'bi-lock-fill';
      default:
        return 'bi-gear-fill';
    }
  }
  private typeFor(action: string): string {
    if (
      action === 'DELETE' ||
      action === 'REJECT' ||
      action === 'LOGIN_FAILED' ||
      action === 'ACCOUNT_LOCKED'
    )
      return 'warning';
    if (action === 'CREATE' || action === 'APPROVE') return 'success';
    return 'info';
  }
  loadNotices(): void {
    this.noticeService.getAllNotices().subscribe({
      next: (data: any) => {
        this.recentNotices = data ? data.slice(0, 4) : [];
        this.cdr.markForCheck();
      },
      error: () => {},
    });
  }
}
