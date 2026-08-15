import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentModel } from '../models/document.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class DocumentsService {
  private apiUrl = `${environment.baseUrl}/api/document`;
  constructor(private http: HttpClient) {}
  createDocument(document: DocumentModel): Observable<DocumentModel> {
    return this.http.post<DocumentModel>(this.apiUrl, document);
  }
  createDocumentWithFile(document: DocumentModel, file: File): Observable<DocumentModel> {
    const formData = new FormData();
    const documentBlob = new Blob([JSON.stringify(document)], { type: 'application/json' });
    formData.append('document', documentBlob);
    formData.append('file', file);
    return this.http.post<DocumentModel>(`${this.apiUrl}/upload`, formData);
  }
  getAllDocuments(): Observable<DocumentModel[]> {
    return this.http.get<DocumentModel[]>(this.apiUrl);
  }
  getDocumentById(id: number): Observable<DocumentModel> {
    return this.http.get<DocumentModel>(`${this.apiUrl}/${id}`);
  }
  updateDocument(id: number, document: DocumentModel): Observable<DocumentModel> {
    return this.http.put<DocumentModel>(`${this.apiUrl}/${id}`, document);
  }
  deleteDocument(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
  getDocumentsByEmployee(employeeId: number): Observable<DocumentModel[]> {
    return this.http.get<DocumentModel[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
}
