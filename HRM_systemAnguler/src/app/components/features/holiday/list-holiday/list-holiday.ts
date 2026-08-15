import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Holiday } from '../../../../models/holiday.model';
import { HolidayService } from '../../../../services/holiday.service';
import { StorageService } from '../../../../services/storage.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-holiday',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-holiday.html',
  styleUrl: './list-holiday.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListHoliday implements OnInit {
  holidays: Holiday[] = [];
  successMessage = '';
  isEmployeeOnly = false;
  constructor(
    private holidayService: HolidayService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadHolidays();
  }
  loadHolidays() {
    this.holidayService.getAllHolidays().subscribe({
      next: (data) => {
        this.holidays = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  deleteHoliday(id: number) {
    if (confirm('Are you sure you want to delete this holiday?')) {
      this.holidayService.deleteHoliday(id).subscribe(() => {
        this.successMessage = 'Holiday deleted successfully';
        this.cdr.markForCheck();
        this.loadHolidays();
      });
    }
  }
}
