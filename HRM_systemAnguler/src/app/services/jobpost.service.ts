import { Injectable } from '@angular/core';
import { JobPost } from '../models/jobpost.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class JobpostService {
  private apiUrl = `${environment.baseUrl}/api/job-posts`;
  constructor(private http: HttpClient) {}
  getAllJobPosts(): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(this.apiUrl);
  }
  getJobPostById(id: number): Observable<JobPost> {
    return this.http.get<JobPost>(`${this.apiUrl}/${id}`);
  }
  saveJobPost(jobPost: JobPost): Observable<JobPost> {
    return this.http.post<JobPost>(this.apiUrl, jobPost);
  }
  updateJobPost(id: number, jobPost: JobPost): Observable<JobPost> {
    return this.http.put<JobPost>(`${this.apiUrl}/${id}`, jobPost);
  }
  deleteJobPost(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
  getByStatus(status: string): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(`${this.apiUrl}/status/${status}`);
  }
  getByDepartment(departmentId: number): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(`${this.apiUrl}/department/${departmentId}`);
  }
  search(keyword: string): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
