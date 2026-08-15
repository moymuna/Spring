import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Attendance } from '../../../../models/attendance.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AttendanceService } from '../../../../services/attendance.service';
import { EmployeeService } from '../../../../services/employee.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-attendance-edit',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './attendance-edit.html',
  styleUrl: './attendance-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceEdit implements OnInit {
  attendance!: Attendance;
  id!: number;
  statuses = ['PRESENT', 'ABSENT', 'HALF_DAY', 'ON_LEAVE', 'HOLIDAY', 'WEEK_OFF', 'WORK_FROM_HOME'];
  employees: EmployeeResponse[] = [];
  constructor(
    private route: ActivatedRoute,
    private attendanceService: AttendanceService,
    private employeeService: EmployeeService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
    this.attendanceService.getAttendanceById(this.id).subscribe((data) => {
      this.attendance = data;
      this.cdr.markForCheck();
    });
  }
  updateAttendance() {
    this.attendanceService.updateAttendance(this.id, this.attendance).subscribe(() => {
      this.toast.success('Attendance Updated');
      this.router.navigate(['/attendance']);
    });
  }
  cancel() {
    this.router.navigate(['/attendance']);
  }
}
