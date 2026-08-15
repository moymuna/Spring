import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LeaveBalance } from '../models/leavebalance.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class LeavebalanceService {
  private apiUrl = `${environment.baseUrl}/api/leave-balance`;
  constructor(private http: HttpClient) {}
  saveLeaveBalance(data: LeaveBalance): Observable<LeaveBalance> {
    return this.http.post<LeaveBalance>(`${this.apiUrl}/save`, data);
  }
  getAllLeaveBalances(): Observable<LeaveBalance[]> {
    return this.http.get<LeaveBalance[]>(`${this.apiUrl}/all`);
  }
  getLeaveBalancesByEmployee(employeeId: number): Observable<LeaveBalance[]> {
    return this.http.get<LeaveBalance[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
  getLeaveBalance(id: number): Observable<LeaveBalance> {
    return this.http.get<LeaveBalance>(`${this.apiUrl}/${id}`);
  }
  updateLeaveBalance(id: number, data: LeaveBalance): Observable<LeaveBalance> {
    return this.http.put<LeaveBalance>(`${this.apiUrl}/update/${id}`, data);
  }
  deleteLeaveBalance(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
  getUtilizationByYear(year: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/stats/utilization?year=${year}`);
  }
}
