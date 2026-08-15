import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DepartmentModel } from '../../../../models/department.model';
import { DepartmentService } from '../../../../services/department.service';
import { Router } from '@angular/router';
import { OfficeService } from '../../../../services/office.service';
import { EmployeeService } from '../../../../services/employee.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-add-department',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-department.html',
  styleUrl: './add-department.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDepartment {
  department: DepartmentModel = {
    departmentName: '',
    code: '',
    officeId: 0,
    departmentHeadId: undefined,
  };
  offices: any[] = [];
  employees: any[] = [];
  constructor(
    private departmentService: DepartmentService,
    private officeService: OfficeService,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private toast: ToastService,
  ) {}
  ngOnInit(): void {
    this.loadOffices();
    this.loadEmployees();
  }
  loadOffices() {
    this.officeService.getAllOffice().subscribe((data) => {
      this.offices = data;
      this.cdr.markForCheck();
    });
  }
  loadEmployees() {
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.cdr.markForCheck();
    });
  }
  save() {
    if (!this.department.departmentName) {
      this.toast.error('Department name is required');
      return;
    }
    if (!this.department.code) {
      this.toast.error('Department code is required');
      return;
    }
    if (!this.department.officeId) {
      this.toast.error('Please select office');
      return;
    }
    this.departmentService.save(this.department).subscribe({
      next: () => {
        this.toast.success('Department Saved Successfully');
        this.router.navigate(['/department']);
      },
      error: (err) => {
        console.log(err);
        this.toast.error('Something went wrong');
      },
    });
  }
  cancel() {
    this.router.navigate(['/department']);
  }
}
