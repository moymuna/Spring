import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Attendance } from '../../../../models/attendance.model';
import { AttendanceService } from '../../../../services/attendance.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-attendance-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ModalOutlet],
  templateUrl: './attendance-list.html',
  styleUrl: './attendance-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceList implements OnInit {
  attendances: Attendance[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private attendanceService: AttendanceService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadAttendance();
  }
  loadAttendance() {
    this.attendanceService.getByPage(this.page, this.size).subscribe({
      next: (data) => {
        this.attendances = data.content ?? data;
        this.totalPages = data.totalPages ?? 1;
        this.totalElements = data.totalElements ?? this.attendances.length;
        this.cdr.markForCheck();
      },
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadAttendance();
      return;
    }
    this.attendanceService.search(this.keyword).subscribe((data) => {
      this.attendances = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadAttendance();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  editAttendance(id: number) {
    this.router.navigate(['/attendance/edit', id]);
  }
  formatWorkedHours(hours: number | null | undefined): string {
    if (!hours) {
      return '-';
    }
    const totalMinutes = Math.round(hours * 60);
    const h = Math.floor(totalMinutes / 60);
    const m = totalMinutes % 60;
    return `${h}h ${m}m`;
  }
  deleteAttendance(id: number) {
    if (confirm('Delete this attendance?')) {
      this.attendanceService.deleteAttendance(id).subscribe(() => {
        this.loadAttendance();
      });
    }
  }
}
