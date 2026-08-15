import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DepartmentModel } from '../../../../models/department.model';
import { ActivatedRoute, Router } from '@angular/router';
import { DepartmentService } from '../../../../services/department.service';
import { OfficeService } from '../../../../services/office.service';
import { EmployeeService } from '../../../../services/employee.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-department',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-department.html',
  styleUrl: './edit-department.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditDepartment implements OnInit {
  department: DepartmentModel = {
    departmentName: '',
    code: '',
    officeId: 0,
    departmentHeadId: undefined,
  };
  id!: number;
  offices: any[] = [];
  employees: any[] = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private departmentService: DepartmentService,
    private officeService: OfficeService,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDepartment();
    this.loadOffices();
    this.loadEmployees();
  }
  loadDepartment() {
    this.departmentService.getById(this.id).subscribe((data) => {
      this.department = data;
      this.cdr.markForCheck();
    });
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
  update() {
    if (!this.department.departmentName) {
      this.toast.error('Department name required');
      return;
    }
    if (!this.department.code) {
      this.toast.error('Department code required');
      return;
    }
    if (!this.department.officeId) {
      this.toast.error('Please select office');
      return;
    }
    this.departmentService.update(this.id, this.department).subscribe({
      next: () => {
        this.toast.success('Department Updated Successfully');
        this.router.navigate(['/department']);
      },
      error: (err) => {
        console.log(err);
        this.toast.error('Update Failed');
      },
    });
  }
  cancel() {
    this.router.navigate(['/department']);
  }
}
