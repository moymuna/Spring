import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { EmployeeService } from '../../../../services/employee.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EmployeeResponse } from '../../../../models/employee.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../../../environments/environment';
import { StorageService } from '../../../../services/storage.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalOutlet],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EmployeeList {
  imageUrl = environment.imgUrl + 'employee/';
  employeePhotoUrl(filename: string | null | undefined): string {
    return filename ? this.storage.appendToken(this.imageUrl + filename) : '';
  }
  employees: EmployeeResponse[] = [];
  loading = false;
  errorMessage = '';
  keyword = '';
  page: number = 0;
  size: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;
  totalEmployee: number = 0;
  activeEmployee: number = 0;
  inactiveEmployee: number = 0;
  departments: any[] = [];
  employeeStatuses = ['ACTIVE', 'ON_LEAVE', 'SUSPENDED', 'RESIGNED', 'TERMINATED'];
  selectedStatus = '';
  selectedDepartment = '';
  constructor(
    private employeeService: EmployeeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private storage: StorageService,
    private route: ActivatedRoute,
  ) {}
  ngOnInit(): void {
    // The topbar search sends people here with ?q=...
    this.route.queryParamMap.subscribe((params) => {
      const q = params.get('q');
      if (q) {
        this.keyword = q;
        this.searchEmployee();
      } else {
        this.loadEmployees();
      }
      this.cdr.markForCheck();
    });
    this.loadCounts();
    this.totalEmployee = this.employees.length;
  }
  loadEmployees() {
    this.loading = true;
    this.employeeService.getEmployeesByPage(this.page, this.size).subscribe({
      next: (response: any) => {
        if (Array.isArray(response)) {
          this.employees = response;
          this.totalPages = 1;
          this.totalElements = response.length;
        } else if (response && response.content) {
          this.employees = response.content;
          this.totalPages = response.totalPages ?? 1;
          this.totalElements = response.totalElements ?? response.content.length;
        } else {
          this.employees = [];
          this.totalPages = 1;
          this.totalElements = 0;
        }
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Employee loading failed', error);
        this.errorMessage = 'Employee loading failed';
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  searchEmployee() {
    if (this.keyword.trim() === '') {
      this.loadEmployees();
      return;
    }
    this.employeeService.searchEmployee(this.keyword).subscribe({
      next: (data) => {
        this.employees = data;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Search failed', error);
        this.errorMessage = 'Search failed';
        this.cdr.markForCheck();
      },
    });
  }
  filterByStatus() {
    if (this.selectedStatus) {
      this.employeeService.getByStatus(this.selectedStatus).subscribe((data) => {
        this.employees = data;
        this.cdr.markForCheck();
      });
    } else {
      this.loadEmployees();
    }
  }
  filterByDepartment() {
    if (this.selectedDepartment) {
      this.employeeService
        .getEmployeesByDepartment(Number(this.selectedDepartment))
        .subscribe((data) => {
          this.employees = data;
          this.cdr.markForCheck();
        });
    } else {
      this.loadEmployees();
    }
  }
  changePage(pageNumber: number) {
    this.page = pageNumber;
    this.loadEmployees();
  }
  loadCounts() {
    this.employeeService.getEmployeeCount().subscribe((data) => {
      this.totalEmployee = data;
      this.cdr.markForCheck();
    });
    this.employeeService.getActiveEmployeeCount().subscribe((data) => {
      this.activeEmployee = data;
      this.cdr.markForCheck();
    });
    this.employeeService.getInactiveEmployeeCount().subscribe((data) => {
      this.inactiveEmployee = data;
      this.cdr.markForCheck();
    });
  }
  deleteEmployee(id: number) {
    let confirmDelete = confirm('Are you sure you want to delete this employee?');
    if (confirmDelete) {
      this.employeeService.deleteEmployee(id).subscribe(() => {
        this.loadEmployees();
        this.loadCounts();
      });
    }
  }
  editEmployee(id: number) {
    this.router.navigate(['/employee/edit', id]);
  }
  viewEmployee(id: number) {
    this.router.navigate(['/employee/view', id]);
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, index) => index);
  }
  getInitials(name: string): string {
    if (!name) return 'UN';
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }
}
