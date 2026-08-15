import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DocumentsService } from '../../../../services/documents.service';
import { DocumentType } from '../../../../models/document.model';
import { Router } from '@angular/router';
import { StorageService } from '../../../../services/storage.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
@Component({
  selector: 'app-add-documents',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-documents.html',
  styleUrl: './add-documents.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDocuments implements OnInit {
  documentForm!: FormGroup;
  selectedFile: File | null = null;
  documentTypes = Object.values(DocumentType);
  successMessage = '';
  errorMessage = '';
  isEmployeeOnly = false;
  selfEmployeeName = '';
  constructor(
    private fb: FormBuilder,
    private documentService: DocumentsService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.documentForm = this.fb.group({
      documentName: ['', Validators.required],
      documentType: ['', Validators.required],
      employeeId: ['', Validators.required],
    });
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    if (this.isEmployeeOnly) {
      this.loadSelfEmployee();
    }
  }
  loadSelfEmployee(): void {
    const userId = this.storage.getUser()?.id;
    if (!userId) {
      return;
    }
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (emp) => {
        this.selfEmployeeName = emp.fullName;
        this.documentForm.get('employeeId')?.setValue(emp.id);
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load your employee profile.';
        this.cdr.markForCheck();
      },
    });
  }
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] ?? null;
  }
  saveDocument() {
    if (this.documentForm.invalid || !this.selectedFile) {
      this.successMessage = '';
      if (!this.selectedFile) {
        this.errorMessage = 'Please choose a file to upload.';
      } else {
        this.errorMessage = 'Please fill in all required fields.';
      }
      this.cdr.markForCheck();
      return;
    }
    this.documentService
      .createDocumentWithFile(this.documentForm.value, this.selectedFile)
      .subscribe({
        next: (response) => {
          this.successMessage = 'Document added successfully';
          this.errorMessage = '';
          this.documentForm.reset();
          this.selectedFile = null;
          this.cdr.markForCheck();
          setTimeout(() => {
            this.router.navigate(['/documents']);
          }, 1000);
        },
        error: (err) => {
          this.successMessage = '';
          this.errorMessage = 'Failed to save document';
          this.cdr.markForCheck();
          console.log(err);
        },
      });
  }
}
