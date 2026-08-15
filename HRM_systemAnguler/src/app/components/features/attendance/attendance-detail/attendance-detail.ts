import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Attendance } from '../../../../models/attendance.model';
import { EmployeeResponse } from '../../../../models/employee.model';
import { AttendanceService } from '../../../../services/attendance.service';
import { KEYS, StorageService } from '../../../../services/storage.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-attendance-detail',
  imports: [CommonModule, FormsModule],
  templateUrl: './attendance-detail.html',
  styleUrl: './attendance-detail.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceDetail implements OnInit {
  attendances: Attendance[] = [];
  employee: EmployeeResponse | null = null;
  currentYear = new Date().getFullYear();
  currentMonth = new Date().getMonth() + 1;
  employeeId: number = 0;
  constructor(
    private attendanceService: AttendanceService,
    private cdr: ChangeDetectorRef,
    private storage: StorageService,
  ) {}
  months = [
    { value: 1, name: 'January' },
    { value: 2, name: 'February' },
    { value: 3, name: 'March' },
    { value: 4, name: 'April' },
    { value: 5, name: 'May' },
    { value: 6, name: 'June' },
    { value: 7, name: 'July' },
    { value: 8, name: 'August' },
    { value: 9, name: 'September' },
    { value: 10, name: 'October' },
    { value: 11, name: 'November' },
    { value: 12, name: 'December' },
  ];
  ngOnInit() {
    this.employee = this.storage.getData(KEYS.EMPLOYEE);
    if (this.employee) {
      this.employeeId = this.employee.id;
    }
    this.loadAttendance();
  }
  loadAttendance() {
    this.attendanceService
      .getAttendanceByMonth(this.employeeId, this.currentYear, this.currentMonth)
      .subscribe((data) => {
        this.attendances = data;
        this.cdr.markForCheck();
      });
  }
}
