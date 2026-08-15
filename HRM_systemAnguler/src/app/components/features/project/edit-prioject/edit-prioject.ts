import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../../../services/project.service';
import { EmployeeService } from '../../../../services/employee.service';
import { OfficeService } from '../../../../services/office.service';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-edit-prioject',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-prioject.html',
  styleUrl: './edit-prioject.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPrioject implements OnInit {
  projectForm!: FormGroup;
  projectId!: number;
  employees: any[] = [];
  offices: any[] = [];
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private employeeService: EmployeeService,
    private officeService: OfficeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.projectForm = this.fb.group({
      projectName: ['', Validators.required],
      description: [''],
      startDate: [''],
      endDate: [''],
      employeeId: [[], Validators.required],
      officeId: ['', Validators.required],
    });
    this.loadEmployees();
    this.loadOffices();
    this.loadProject();
  }
  loadEmployees() {
    this.employeeService.getAllEmployees().subscribe({
      next: (res: any) => {
        this.employees = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  loadOffices() {
    this.officeService.getAllOffice().subscribe({
      next: (res: any) => {
        this.offices = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  loadProject() {
    this.projectService.getProjectById(this.projectId).subscribe({
      next: (res: any) => {
        this.projectForm.patchValue({
          projectName: res.projectName,
          description: res.description,
          startDate: res.startDate?.substring(0, 10),
          endDate: res.endDate?.substring(0, 10),
          officeId: res.officeId,
          employeeId: res.employeeId ?? [],
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  updateProject() {
    if (this.projectForm.invalid) {
      return;
    }
    this.projectService.updateProject(this.projectId, this.projectForm.value).subscribe({
      next: (res) => {
        this.successMessage = 'Project updated successfully';
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/project']);
        }, 1000);
      },
      error: (err) => {
        this.errorMessage = 'Project update failed';
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  cancel() {
    this.router.navigate(['/project']);
  }
}
