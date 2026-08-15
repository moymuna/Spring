import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { EmployeeService } from '../../../../services/employee.service';
import { ActivatedRoute } from '@angular/router';
import { EmployeeResponse } from '../../../../models/employee.model';
import { environment } from '../../../../../environments/environment';
import { CommonModule } from '@angular/common';
import { StorageService } from '../../../../services/storage.service';
@Component({
  selector: 'app-employee-detail',
  imports: [CommonModule],
  templateUrl: './employee-detail.html',
  styleUrl: './employee-detail.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EmployeeDetail {
  employee?: EmployeeResponse;
  imageUrl = environment.imgUrl + 'employee/';
  loading = false;
  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
    private storage: StorageService,
  ) {}
  employeePhotoUrl(filename: string | null | undefined): string {
    return filename ? this.storage.appendToken(this.imageUrl + filename) : '';
  }
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadEmployee(id);
    }
  }
  loadEmployee(id: number) {
    this.loading = true;
    this.employeeService.getEmployeeById(id).subscribe({
      next: (data) => {
        this.employee = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  getInitials(name: string): string {
    if (!name) return 'NA';
    return name
      .split(' ')
      .map((word) => word.charAt(0))
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }
}
