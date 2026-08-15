import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DesignationModel } from '../../../../models/designation.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DesignationService } from '../../../../services/designation.service';
import { DepartmentService } from '../../../../services/department.service';
import { DepartmentModel } from '../../../../models/department.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-designation',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-designation.html',
  styleUrl: './edit-designation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditDesignation implements OnInit {
  id!: number;
  designation: DesignationModel = {
    title: '',
    level: '',
    departmentId: 0,
  };
  departments: DepartmentModel[] = [];
  constructor(
    private designationService: DesignationService,
    private departmentService: DepartmentService,
    private route: ActivatedRoute,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.departmentService.getAll().subscribe((data) => {
      this.departments = data;
      this.cdr.markForCheck();
    });
    this.loadDesignation();
  }
  loadDesignation() {
    this.designationService.getById(this.id).subscribe({
      next: (data) => {
        this.designation = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.toast.error(err.error?.message || 'Failed to load designation.');
      },
    });
  }
  update() {
    this.designationService.update(this.id, this.designation).subscribe(() => {
      this.toast.success('Designation Updated Successfully');
      this.router.navigate(['/designation']);
    });
  }
  cancel() {
    this.router.navigate(['/designation']);
  }
}
