import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Advance } from '../models/advance.model';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AdvanceService {
  private apiUrl = `${environment.baseUrl}/api/advances`;
  constructor(private http: HttpClient) {}
  saveAdvance(data: Advance): Observable<Advance> {
    return this.http.post<Advance>(`${this.apiUrl}/save`, data);
  }
  getAllAdvances(): Observable<Advance[]> {
    return this.http.get<Advance[]>(`${this.apiUrl}/all`);
  }
  getAdvancesByEmployee(employeeId: number): Observable<Advance[]> {
    return this.http.get<Advance[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
  getAdvancesByStatus(status: string): Observable<Advance[]> {
    return this.http.get<Advance[]>(`${this.apiUrl}/status/${status}`);
  }
  getAdvance(id: number): Observable<Advance> {
    return this.http.get<Advance>(`${this.apiUrl}/${id}`);
  }
  updateAdvance(id: number, data: Advance): Observable<Advance> {
    return this.http.put<Advance>(`${this.apiUrl}/update/${id}`, data);
  }
  deleteAdvance(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' as 'json' });
  }
  approveAdvance(id: number): Observable<Advance> {
    return this.http.put<Advance>(`${this.apiUrl}/approve/${id}`, {});
  }
  rejectAdvance(id: number, reason: string): Observable<Advance> {
    return this.http.put<Advance>(
      `${this.apiUrl}/reject/${id}?rejectionReason=${encodeURIComponent(reason)}`,
      {},
    );
  }
  markAsPaid(id: number): Observable<Advance> {
    return this.http.put<Advance>(`${this.apiUrl}/pay/${id}`, {});
  }
  recordRecovery(id: number, amount: number): Observable<Advance> {
    return this.http.put<Advance>(`${this.apiUrl}/recover/${id}?amount=${amount}`, {});
  }
  search(keyword: string): Observable<Advance[]> {
    return this.http.get<Advance[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getPendingCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/pending`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
