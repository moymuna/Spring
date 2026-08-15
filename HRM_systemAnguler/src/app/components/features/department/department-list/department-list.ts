import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { DepartmentModel } from '../../../../models/department.model';
import { DepartmentService } from '../../../../services/department.service';
import { ToastService } from '../../../../services/toast.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-department-list',
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './department-list.html',
  styleUrl: './department-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DepartmentList implements OnInit {
  departments: DepartmentModel[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private service: DepartmentService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private toast: ToastService,
  ) {}
  ngOnInit(): void {
    this.loadDepartments();
  }
  loadDepartments() {
    this.service.getByPage(this.page, this.size).subscribe((data) => {
      this.departments = data.content ?? data;
      this.totalPages = data.totalPages ?? 1;
      this.totalElements = data.totalElements ?? this.departments.length;
      this.cdr.markForCheck();
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadDepartments();
      return;
    }
    this.service.search(this.keyword).subscribe((data) => {
      this.departments = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadDepartments();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  edit(id: number) {
    this.router.navigate(['/department/edit', id]);
  }
  details(id: number) {
    this.router.navigate(['/department/details', id]);
  }
  delete(id: number) {
    if (confirm('Delete this Department?')) {
      this.service.delete(id).subscribe(() => {
        this.toast.success('Department deleted successfully.');
        this.loadDepartments();
      });
    }
  }
}
