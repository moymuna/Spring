import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ProjectModel } from '../../../../models/project.model';
import { ProjectService } from '../../../../services/project.service';
import { ToastService } from '../../../../services/toast.service';
import { StorageService } from '../../../../services/storage.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-prioject',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-prioject.html',
  styleUrl: './list-prioject.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListPrioject implements OnInit {
  projects: ProjectModel[] = [];
  errorMessage = '';
  isEmployeeOnly = false;
  constructor(
    private projectService: ProjectService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadProjects();
  }
  loadProjects() {
    if (this.storage.getRole() === 'EMPLOYEE') {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        this.errorMessage = 'Failed to load projects';
        this.cdr.markForCheck();
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (employee) => {
          this.projectService.getByEmployeeId(employee.id!).subscribe({
            next: (res: any) => {
              this.projects = res;
              this.cdr.markForCheck();
            },
            error: () => {
              this.errorMessage = 'Failed to load projects';
              this.cdr.markForCheck();
            },
          });
        },
        error: () => {
          this.errorMessage = 'Failed to load projects';
          this.cdr.markForCheck();
        },
      });
      return;
    }
    this.projectService.getAllProjects().subscribe({
      next: (res: any) => {
        this.projects = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Failed to load projects';
        this.cdr.markForCheck();
      },
    });
  }
  editProject(id: number) {
    this.router.navigate(['/project/edit', id]);
  }
  deleteProject(id: number) {
    if (confirm('Are you sure to delete this project?')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          this.toast.success('Project deleted successfully');
          this.loadProjects();
        },
        error: (err) => {
          console.log(err);
          this.toast.error('Delete failed');
          this.cdr.markForCheck();
        },
      });
    }
  }
}
