import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { OfficeResponse } from '../../../../models/office.model';
import { OfficeService } from '../../../../services/office.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-office',
  imports: [CommonModule, FormsModule, RouterLink, ModalOutlet],
  templateUrl: './list-office.html',
  styleUrl: './list-office.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListOffice {
  offices: OfficeResponse[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private officeService: OfficeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.loadOffice();
  }
  loadOffice() {
    this.officeService.getByPage(this.page, this.size).subscribe({
      next: (data) => {
        this.offices = data.content ?? data;
        this.totalPages = data.totalPages ?? 1;
        this.totalElements = data.totalElements ?? this.offices.length;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadOffice();
      return;
    }
    this.officeService.search(this.keyword).subscribe((data) => {
      this.offices = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadOffice();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  deleteOffice(id: number) {
    if (confirm('Are you sure?')) {
      this.officeService.deleteOffice(id).subscribe(() => {
        this.loadOffice();
      });
    }
  }
}
