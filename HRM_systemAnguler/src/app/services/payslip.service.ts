import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Payslip } from '../models/payslip.model';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class PayslipService {
  private apiUrl = `${environment.baseUrl}/api/payslip`;
  constructor(private http: HttpClient) {}
  createPayslip(payslip: Payslip): Observable<Payslip> {
    return this.http.post<Payslip>(this.apiUrl, payslip);
  }
  getAllPayslips(): Observable<Payslip[]> {
    return this.http.get<Payslip[]>(this.apiUrl);
  }
  getPayslipsByEmployee(employeeId: number): Observable<Payslip[]> {
    return this.http.get<Payslip[]>(`${this.apiUrl}/employee/${employeeId}`);
  }
  getPayslipById(id: number): Observable<Payslip> {
    return this.http.get<Payslip>(`${this.apiUrl}/${id}`);
  }
  updatePayslip(id: number, payslip: Payslip): Observable<Payslip> {
    return this.http.put<Payslip>(`${this.apiUrl}/${id}`, payslip);
  }
  deletePayslip(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
}
