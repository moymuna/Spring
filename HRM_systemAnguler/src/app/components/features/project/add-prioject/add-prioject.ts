import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProjectService } from '../../../../services/project.service';
@Component({
  selector: 'app-add-prioject',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-prioject.html',
  styleUrl: './add-prioject.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddPrioject implements OnInit {
  projectForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.projectForm = this.fb.group({
      projectName: ['', Validators.required],
      description: [''],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      employeeId: [[], Validators.required],
      officeId: ['', Validators.required],
    });
  }
  saveProject() {
    if (this.projectForm.invalid) {
      return;
    }
    let data = this.projectForm.value;
    data.employeeId = data.employeeId
      .toString()
      .split(',')
      .map((id: string) => Number(id));
    this.projectService.createProject(data).subscribe({
      next: (res) => {
        this.successMessage = 'Project created successfully';
        this.projectForm.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/project']);
        }, 800);
      },
      error: (err) => {
        this.errorMessage = 'Project create failed';
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
}
