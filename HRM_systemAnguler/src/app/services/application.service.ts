import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationModel } from '../models/application.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  private apiUrl = `${environment.baseUrl}/api/applications`;
  constructor(private http: HttpClient) {}
  applyJob(data: ApplicationModel): Observable<ApplicationModel> {
    return this.http.post<ApplicationModel>(`${this.apiUrl}/apply`, data);
  }
  getAll(): Observable<ApplicationModel[]> {
    return this.http.get<ApplicationModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<ApplicationModel> {
    return this.http.get<ApplicationModel>(`${this.apiUrl}/${id}`);
  }
  updateStatus(id: number, status: string): Observable<ApplicationModel> {
    return this.http.put<ApplicationModel>(`${this.apiUrl}/${id}/status?status=${status}`, {});
  }
  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
  search(keyword: string): Observable<ApplicationModel[]> {
    return this.http.get<ApplicationModel[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
