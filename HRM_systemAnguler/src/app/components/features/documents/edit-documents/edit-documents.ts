import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DocumentsService } from '../../../../services/documents.service';
import { DocumentType } from '../../../../models/document.model';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-edit-documents',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-documents.html',
  styleUrl: './edit-documents.css',
})
export class EditDocuments implements OnInit {
  documentForm!: FormGroup;
  id!: number;
  documentTypes = Object.values(DocumentType);
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private documentService: DocumentsService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.documentForm = this.fb.group({
      documentName: ['', Validators.required],
      documentType: ['', Validators.required],
      filePath: ['', Validators.required],
      employeeId: ['', Validators.required],
    });
    this.loadDocument();
  }
  loadDocument() {
    this.documentService.getDocumentById(this.id).subscribe({
      next: (data) => {
        this.documentForm.patchValue(data);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  updateDocument() {
    if (this.documentForm.invalid) {
      return;
    }
    this.documentService.updateDocument(this.id, this.documentForm.value).subscribe({
      next: () => {
        this.successMessage = 'Document updated successfully';
      },
      error: (err) => {
        this.errorMessage = 'Update failed';
        console.log(err);
      },
    });
  }
}
