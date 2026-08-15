import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Attendance } from '../models/attendance.model';
import { Observable } from 'rxjs';
import { silentContext } from '../interceptors/error.interceptor';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AttendanceService {
  private apiUrl = `${environment.baseUrl}/api/attendance`;
  constructor(private http: HttpClient) {}
  saveAttendance(attendance: Attendance): Observable<Attendance> {
    return this.http.post<Attendance>(`${this.apiUrl}/save`, attendance);
  }
  getAttendanceById(id: number): Observable<Attendance> {
    return this.http.get<Attendance>(`${this.apiUrl}/${id}`);
  }
  getAllAttendance(): Observable<Attendance[]> {
    return this.http.get<Attendance[]>(`${this.apiUrl}/all`);
  }
  updateAttendance(id: number, attendance: Attendance): Observable<Attendance> {
    return this.http.put<Attendance>(`${this.apiUrl}/update/${id}`, attendance);
  }
  deleteAttendance(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, {
      responseType: 'text',
    });
  }
  clockIn(employeeId: number) {
    return this.http.post<Attendance>(`${this.apiUrl}/clock-in/${employeeId}`, {});
  }
  clockOut(employeeId: number) {
    return this.http.put<Attendance>(`${this.apiUrl}/clock-out/${employeeId}`, {});
  }
  getTodayAttendance(employeeId: number) {
    return this.http.get<Attendance>(`${this.apiUrl}/today/${employeeId}`, {
      context: silentContext(),
    });
  }
  getAttendanceByMonth(employeeId: number, year: number, month: number, context?: HttpContext) {
    return this.http.get<Attendance[]>(`${this.apiUrl}/employee/${employeeId}`, {
      params: {
        year,
        month,
      },
      context,
    });
  }
  search(keyword: string): Observable<Attendance[]> {
    return this.http.get<Attendance[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
  getMonthlySummary(employeeId: number, year: number, month: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/employee/${employeeId}/summary`, {
      params: { year, month },
    });
  }
  getTodayAttendanceCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/today`);
  }
}
