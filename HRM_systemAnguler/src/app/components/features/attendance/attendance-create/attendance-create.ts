import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AttendanceService } from '../../../../services/attendance.service';
import { Attendance } from '../../../../models/attendance.model';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-attendance-create',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './attendance-create.html',
  styleUrl: './attendance-create.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceCreate {
  attendance: Attendance = {
    date: '',
    checkInTime: '',
    checkOutTime: '',
    status: '',
    employeeId: 0,
  };
  statuses = ['PRESENT', 'ABSENT', 'HALF_DAY', 'ON_LEAVE', 'HOLIDAY', 'WEEK_OFF', 'WORK_FROM_HOME'];
  employees: EmployeeResponse[] = [];
  constructor(
    private attendanceService: AttendanceService,
    private employeeService: EmployeeService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
  }
  saveAttendance() {
    this.attendanceService.saveAttendance(this.attendance).subscribe({
      next: (res) => {
        this.toast.success('Attendance Saved');
        this.router.navigate(['/attendance']);
      },
      error: (err) => {
        console.log(err);
        this.toast.error('Attendance Save Failed');
      },
    });
  }
  cancel() {
    this.router.navigate(['/attendance']);
  }
}
