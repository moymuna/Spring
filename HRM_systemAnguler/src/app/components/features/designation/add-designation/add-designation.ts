import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DesignationService } from '../../../../services/designation.service';
import { DesignationModel } from '../../../../models/designation.model';
import { DepartmentService } from '../../../../services/department.service';
import { DepartmentModel } from '../../../../models/department.model';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-add-designation',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-designation.html',
  styleUrl: './add-designation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDesignation {
  designation: DesignationModel = {
    title: '',
    level: '',
    departmentId: 0,
  };
  departments: DepartmentModel[] = [];
  ngOnInit() {
    this.departmentService.getAll().subscribe((data) => {
      this.departments = data;
      this.cdr.markForCheck();
    });
  }
  constructor(
    private designationService: DesignationService,
    private departmentService: DepartmentService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  save() {
    this.designationService.save(this.designation).subscribe(() => {
      this.toast.success('Designation Saved Successfully');
      this.reset();
      this.cdr.markForCheck();
      this.router.navigate(['/designation']);
    });
  }
  reset() {
    this.designation = {
      title: '',
      level: '',
      departmentId: 0,
    };
  }
  cancel() {
    this.router.navigate(['/designation']);
  }
}
