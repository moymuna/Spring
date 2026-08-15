import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DocumentModel } from '../../../../models/document.model';
import { DocumentsService } from '../../../../services/documents.service';
import { ToastService } from '../../../../services/toast.service';
import { StorageService } from '../../../../services/storage.service';
import { EmployeeService } from '../../../../services/employee.service';
import { environment } from '../../../../../environments/environment';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-documents',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-documents.html',
  styleUrl: './list-documents.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListDocuments implements OnInit {
  documents: DocumentModel[] = [];
  loadFailed = false;
  isEmployeeOnly = false;
  constructor(
    private documentService: DocumentsService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadDocuments();
  }
  documentUrl(filePath: string): string {
    return this.storage.appendToken(`${environment.imgUrl}${filePath}`);
  }
  loadDocuments() {
    this.loadFailed = false;
    if (this.isEmployeeOnly) {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        this.loadFailed = true;
        this.cdr.markForCheck();
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (employee) => {
          this.documentService.getDocumentsByEmployee(employee.id!).subscribe({
            next: (data) => {
              this.documents = data;
              this.cdr.markForCheck();
            },
            error: () => {
              this.loadFailed = true;
              this.toast.error('Could not load documents. Please try again.');
              this.cdr.markForCheck();
            },
          });
        },
        error: () => {
          this.loadFailed = true;
          this.toast.error('Could not load documents. Please try again.');
          this.cdr.markForCheck();
        },
      });
      return;
    }
    this.documentService.getAllDocuments().subscribe({
      next: (data) => {
        this.documents = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.loadFailed = true;
        this.toast.error('Could not load documents. Please try again.');
        this.cdr.markForCheck();
      },
    });
  }
  deleteDocument(id: number) {
    if (confirm('Are you sure delete this document?')) {
      this.documentService.deleteDocument(id).subscribe(() => {
        this.loadDocuments();
      });
    }
  }
}
