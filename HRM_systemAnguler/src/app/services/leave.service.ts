import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Leave } from '../models/leave.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class LeaveService {
  private apiUrl = `${environment.baseUrl}/api/leaves`;
  constructor(private http: HttpClient) {}
  saveLeave(data: Leave): Observable<Leave> {
    return this.http.post<Leave>(`${this.apiUrl}/save`, data);
  }
  getAllLeaves(): Observable<Leave[]> {
    return this.http.get<Leave[]>(`${this.apiUrl}/all`);
  }
  getLeavesByEmployee(employeeId: number): Observable<Leave[]> {
    return this.http.get<Leave[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
  getLeave(id: number): Observable<Leave> {
    return this.http.get<Leave>(`${this.apiUrl}/${id}`);
  }
  updateLeave(id: number, data: Leave): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/update/${id}`, data);
  }
  deleteLeave(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
  approveLeave(id: number): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/approve/${id}`, {});
  }
  rejectLeave(id: number, reason: string): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/reject/${id}?rejectionReason=${reason}`, {});
  }
  cancelLeave(id: number): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/cancel/${id}`, {});
  }
  search(keyword: string): Observable<Leave[]> {
    return this.http.get<Leave[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
