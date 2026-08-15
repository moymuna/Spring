import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApplicationModel } from '../../../../models/application.model';
import { ApplicationService } from '../../../../services/application.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-application',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './list-application.html',
  styleUrl: './list-application.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListApplication {
  applications: ApplicationModel[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private service: ApplicationService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.load();
  }
  load() {
    this.service.getByPage(this.page, this.size).subscribe((data) => {
      this.applications = data.content ?? data;
      this.totalPages = data.totalPages ?? 1;
      this.totalElements = data.totalElements ?? this.applications.length;
      this.cdr.markForCheck();
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.load();
      return;
    }
    this.service.search(this.keyword).subscribe((data) => {
      this.applications = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.load();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  delete(id: number) {
    if (confirm('Delete Application?')) {
      this.service.delete(id).subscribe(() => {
        this.load();
      });
    }
  }
}
