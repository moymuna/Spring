import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LeaveType } from '../models/leavetype.model';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class LeaveTypeService {
  private apiUrl = `${environment.baseUrl}/api/leave-types`;
  constructor(private http: HttpClient) {}
  saveLeaveType(data: LeaveType): Observable<LeaveType> {
    return this.http.post<LeaveType>(`${this.apiUrl}/save`, data);
  }
  getAllLeaveTypes(): Observable<LeaveType[]> {
    return this.http.get<LeaveType[]>(`${this.apiUrl}/all`);
  }
  getLeaveTypeById(id: number): Observable<LeaveType> {
    return this.http.get<LeaveType>(`${this.apiUrl}/${id}`);
  }
  updateLeaveType(id: number, data: LeaveType): Observable<LeaveType> {
    return this.http.put<LeaveType>(`${this.apiUrl}/update/${id}`, data);
  }
  deleteLeaveType(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
}
