import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
export interface AdminDashboard {
  totalEmployees: number;
  activeEmployees: number;
  inactiveEmployees: number;
  departments: number;
  onLeaveToday: number;
  newHiresThisMonth: number;
  pendingLeaveApprovals: number;
  pendingAdvanceApprovals: number;
  headcountByDepartment: Record<string, number>;
  attendanceToday: Record<string, number>;
  headcountTrend: Record<string, number>;
}
export interface Birthday {
  employeeId: number;
  employeeName: string;
  employeeCode: string;
  image?: string;
  nextBirthday: string;
  daysUntil: number;
}
@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private apiUrl = `${environment.baseUrl}/api/dashboard`;
  constructor(private http: HttpClient) {}
  getAdminDashboard(year?: number): Observable<AdminDashboard> {
    const query = year ? `?year=${year}` : '';
    return this.http.get<AdminDashboard>(`${this.apiUrl}/admin${query}`);
  }
  getUpcomingBirthdays(withinDays = 30): Observable<Birthday[]> {
    return this.http.get<Birthday[]>(`${this.apiUrl}/birthdays?withinDays=${withinDays}`);
  }
}
