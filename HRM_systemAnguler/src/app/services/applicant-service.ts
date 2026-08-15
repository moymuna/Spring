import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Applicant } from '../models/applicant.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class ApplicantService {
  private apiUrl = `${environment.baseUrl}/api/applicants`;
  constructor(private http: HttpClient) {}
  createApplicant(applicant: Applicant): Observable<Applicant> {
    return this.http.post<Applicant>(this.apiUrl, applicant);
  }
  getAllApplicants(): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(this.apiUrl);
  }
  getApplicantById(id: number): Observable<Applicant> {
    return this.http.get<Applicant>(`${this.apiUrl}/${id}`);
  }
  updateApplicant(id: number, applicant: Applicant): Observable<Applicant> {
    return this.http.put<Applicant>(`${this.apiUrl}/${id}`, applicant);
  }
  deleteApplicant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  search(keyword: string): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
