import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { KEYS, StorageService } from '../../../../services/storage.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { AuthService } from '../../../../services/auth.service';
import { AttendanceService } from '../../../../services/attendance.service';
import { Attendance } from '../../../../models/attendance.model';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { HolidayService } from '../../../../services/holiday.service';
import { Holiday } from '../../../../models/holiday.model';
import { LeavebalanceService } from '../../../../services/leavebalance.service';
import { AdvanceService } from '../../../../services/advance.service';
import { LeaveService } from '../../../../services/leave.service';
import { DashboardService, Birthday } from '../../../../services/dashboard.service';
interface CalendarCell {
  day: number | null;
  date?: string;
  status?: string;
  today?: boolean;
}
@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './employee-dashboard.html',
  styleUrl: './employee-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EmployeeDashboard implements OnInit {
  imageUrl = environment.imgUrl + 'employee/';
  employeePhotoUrl(filename: string | null | undefined): string {
    return filename ? this.storage.appendToken(this.imageUrl + filename) : '';
  }
  user: any = null;
  clockedIn = false;
  completedToday = false;
  clockInTime: string | null = null;
  checkOutTime: string | null = null;
  notices: any[] = [];
  upcomingHolidays: Holiday[] = [];
  birthdays: Birthday[] = [];
  leaveBalances: any[] = [];
  pendingRequests = 0;
  errorMessage: string | null = null;
  employee: EmployeeResponse | null = null;
  userId!: number;
  todayAttendance: Attendance | null = null;
  monthlySummary: any = null;
  calendar: CalendarCell[] = [];
  calendarLabel = '';
  today = new Date();
  weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  constructor(
    private storage: StorageService,
    private attendanceService: AttendanceService,
    private auth: AuthService,
    private http: HttpClient,
    private employeeService: EmployeeService,
    private holidayService: HolidayService,
    private leaveBalanceService: LeavebalanceService,
    private advanceService: AdvanceService,
    private leaveService: LeaveService,
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const session = this.storage.getUser();
    if (session) {
      this.loadUserProfile(session.id);
      this.userId = session.id;
    }
    this.loadNotices();
    this.loadUpcomingHolidays();
    this.loadBirthdays();
    this.loadEmployee();
  }
  loadEmployee() {
    this.employeeService.getByUserId(this.userId, silentContext()).subscribe({
      next: (data) => {
        this.employee = data;
        this.storage.saveData(KEYS.EMPLOYEE, data);
        this.loadTodayAttendance();
        this.loadMonthlySummary();
        this.loadCalendar();
        this.loadLeaveBalances();
        this.loadPendingRequests();
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  /** Builds a Monday-first month grid and colours each day by its attendance status. */
  loadCalendar(): void {
    if (!this.employee) {
      return;
    }
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    this.calendarLabel = now.toLocaleDateString(undefined, { month: 'long', year: 'numeric' });
    this.attendanceService
      .getAttendanceByMonth(this.employee.id!, year, month, silentContext())
      .subscribe({
        next: (records) => {
          const byDate = new Map<string, string>();
          (records ?? []).forEach((r: any) => {
            if (r.date) {
              byDate.set(String(r.date).substring(0, 10), r.status);
            }
          });
          this.calendar = this.buildGrid(year, month, byDate);
          this.cdr.markForCheck();
        },
        error: () => {
          this.calendar = this.buildGrid(year, month, new Map());
          this.cdr.markForCheck();
        },
      });
  }
  private buildGrid(year: number, month: number, byDate: Map<string, string>): CalendarCell[] {
    const first = new Date(year, month - 1, 1);
    const daysInMonth = new Date(year, month, 0).getDate();
    // JS weeks start on Sunday; shift so Monday is the first column.
    const lead = (first.getDay() + 6) % 7;
    const cells: CalendarCell[] = [];
    for (let i = 0; i < lead; i++) {
      cells.push({ day: null });
    }
    const todayIso = new Date().toISOString().substring(0, 10);
    for (let day = 1; day <= daysInMonth; day++) {
      const iso = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      cells.push({ day, date: iso, status: byDate.get(iso), today: iso === todayIso });
    }
    return cells;
  }
  statusClass(status?: string): string {
    switch (status) {
      case 'PRESENT':
      case 'WORK_FROM_HOME':
        return 'present';
      case 'HALF_DAY':
        return 'late';
      case 'ABSENT':
        return 'absent';
      case 'HOLIDAY':
      case 'WEEK_OFF':
      case 'ON_LEAVE':
        return 'holiday';
      default:
        return '';
    }
  }
  loadLeaveBalances(): void {
    if (!this.employee) {
      return;
    }
    this.leaveBalanceService.getLeaveBalancesByEmployee(this.employee.id!).subscribe({
      next: (data: any) => {
        this.leaveBalances = data ?? [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.leaveBalances = [];
        this.cdr.markForCheck();
      },
    });
  }
  usedPercent(balance: any): number {
    const total = Number(balance?.totalEntitled) || 0;
    if (!total) {
      return 0;
    }
    return Math.min(100, Math.round((Number(balance?.used) / total) * 100));
  }
  get totalRemainingLeave(): number {
    return this.leaveBalances.reduce((sum, b) => sum + (Number(b.remaining) || 0), 0);
  }
  /** "Pending tasks" in the reference design; here it's the person's own open requests. */
  loadPendingRequests(): void {
    if (!this.employee) {
      return;
    }
    let count = 0;
    this.leaveService.getLeavesByEmployee(this.employee.id!).subscribe({
      next: (leaves) => {
        count += (leaves ?? []).filter((l: any) => l.status === 'PENDING').length;
        this.pendingRequests = count;
        this.cdr.markForCheck();
      },
    });
    this.advanceService.getAdvancesByEmployee(this.employee.id!).subscribe({
      next: (advances) => {
        count += (advances ?? []).filter((a: any) => a.status === 'PENDING').length;
        this.pendingRequests = count;
        this.cdr.markForCheck();
      },
    });
  }
  get nextPayrollDate(): Date {
    const now = new Date();
    return new Date(now.getFullYear(), now.getMonth() + 1, 0);
  }
  loadUpcomingHolidays(): void {
    this.holidayService.getUpcomingHolidays(5).subscribe({
      next: (data) => {
        this.upcomingHolidays = data ?? [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.upcomingHolidays = [];
        this.cdr.markForCheck();
      },
    });
  }
  daysUntil(dateValue: string | undefined): number {
    if (!dateValue) {
      return 0;
    }
    const target = new Date(dateValue).getTime();
    const today = new Date().setHours(0, 0, 0, 0);
    return Math.max(0, Math.round((target - today) / 86400000));
  }
  loadBirthdays(): void {
    this.dashboardService.getUpcomingBirthdays(30).subscribe({
      next: (data) => {
        this.birthdays = data ?? [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.birthdays = [];
        this.cdr.markForCheck();
      },
    });
  }
  loadUserProfile(userId: number): void {
    this.http.get<any>(`${environment.apiUrl}user/${userId}`).subscribe({
      next: (data) => {
        this.user = data;
        this.cdr.markForCheck();
      },
    });
  }
  loadNotices(): void {
    this.http.get<any[]>(`${environment.apiUrl}notice`).subscribe({
      next: (data) => {
        this.notices = data ? data.slice(0, 3) : [];
        this.cdr.markForCheck();
      },
    });
  }
  toggleClock() {
    if (!this.employee) {
      return;
    }
    if (!this.clockedIn) {
      this.attendanceService.clockIn(this.employee.id!).subscribe({
        next: (attendance) => {
          this.todayAttendance = attendance;
          this.clockedIn = true;
          this.clockInTime = attendance.checkInTime!;
          this.loadCalendar();
          this.cdr.markForCheck();
        },
      });
    } else {
      this.attendanceService.clockOut(this.employee.id!).subscribe({
        next: (attendance) => {
          this.todayAttendance = attendance;
          this.clockedIn = false;
          this.clockInTime = null;
          this.completedToday = true;
          this.checkOutTime = attendance.checkOutTime!;
          this.loadCalendar();
          this.cdr.markForCheck();
        },
      });
    }
  }
  loadTodayAttendance() {
    if (!this.employee) {
      return;
    }
    this.attendanceService.getTodayAttendance(this.employee.id!).subscribe({
      next: (attendance) => {
        this.todayAttendance = attendance;
        if (attendance && attendance.checkInTime && !attendance.checkOutTime) {
          this.clockedIn = true;
          this.completedToday = false;
          this.clockInTime = attendance.checkInTime;
        } else if (attendance && attendance.checkOutTime) {
          this.clockedIn = false;
          this.completedToday = true;
          this.clockInTime = null;
          this.checkOutTime = attendance.checkOutTime;
        } else {
          this.clockedIn = false;
          this.completedToday = false;
          this.clockInTime = null;
        }
        this.cdr.markForCheck();
      },
      error: () => {
        this.clockedIn = false;
        this.completedToday = false;
        this.clockInTime = null;
        this.cdr.markForCheck();
      },
    });
  }
  loadMonthlySummary() {
    if (!this.employee) {
      return;
    }
    const now = new Date();
    this.attendanceService
      .getMonthlySummary(this.employee.id!, now.getFullYear(), now.getMonth() + 1)
      .subscribe({
        next: (summary) => {
          this.monthlySummary = summary;
          this.cdr.markForCheck();
        },
      });
  }
  logout(): void {
    this.auth.logout();
    this.storage.removeData(KEYS.EMPLOYEE);
  }
}
