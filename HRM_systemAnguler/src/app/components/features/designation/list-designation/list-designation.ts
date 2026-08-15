import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { DesignationModel } from '../../../../models/designation.model';
import { DesignationService } from '../../../../services/designation.service';
import { ToastService } from '../../../../services/toast.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-designation',
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './list-designation.html',
  styleUrl: './list-designation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListDesignation implements OnInit {
  designations: DesignationModel[] = [];
  keyword = '';
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  constructor(
    private designationService: DesignationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit(): void {
    this.loadDesignations();
  }
  loadDesignations() {
    this.designationService.getByPage(this.page, this.size).subscribe((data) => {
      this.designations = data.content ?? data;
      this.totalPages = data.totalPages ?? 1;
      this.totalElements = data.totalElements ?? this.designations.length;
      this.cdr.markForCheck();
    });
  }
  search() {
    if (this.keyword.trim() === '') {
      this.loadDesignations();
      return;
    }
    this.designationService.search(this.keyword).subscribe((data) => {
      this.designations = data;
      this.cdr.markForCheck();
    });
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadDesignations();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
  delete(id: number) {
    if (confirm('Are you sure to delete this designation?')) {
      this.designationService.delete(id).subscribe(() => {
        this.toast.success('Designation deleted successfully');
        this.loadDesignations();
      });
    }
  }
  edit(id: number) {
    this.router.navigate(['/designation/edit', id]);
  }
  add() {
    this.router.navigate(['/designation/add']);
  }
}
