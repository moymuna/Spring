import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Payroll } from '../models/payroll.model';
export interface SalarySheetRow {
  employeeId: number;
  employeeCode: string;
  employeeName: string;
  department?: string;
  designation?: string;
  gradeNumber?: number;
  basicSalary: number;
  hra: number;
  conveyanceAllowance: number;
  medicalAllowance: number;
  specialAllowance: number;
  grossSalary: number;
  providentFund: number;
  professionalTax: number;
  incomeTax: number;
  lopDays: number;
  unpaidLeaveDays: number;
  leaveDeduction: number;
  advanceDeduction: number;
  totalDeductions: number;
  netSalary: number;
  paidDays?: number;
  status: string;
  payrollId?: number;
}
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class PayrollService {
  private apiUrl = `${environment.baseUrl}/api/payroll`;
  constructor(private http: HttpClient) {}
  createPayroll(payroll: Payroll): Observable<Payroll> {
    return this.http.post<Payroll>(this.apiUrl, payroll);
  }
  getPayrollById(id: number): Observable<Payroll> {
    return this.http.get<Payroll>(`${this.apiUrl}/${id}`);
  }
  getAllPayrolls(): Observable<Payroll[]> {
    return this.http.get<Payroll[]>(this.apiUrl);
  }
  getSalarySheet(year: number, month: number): Observable<SalarySheetRow[]> {
    return this.http.get<SalarySheetRow[]>(`${this.apiUrl}/sheet?year=${year}&month=${month}`);
  }
  getPayrollsByEmployee(employeeId: number, context?: HttpContext): Observable<Payroll[]> {
    return this.http.get<Payroll[]>(`${this.apiUrl}/employee/${employeeId}`, { context });
  }
  updatePayroll(id: number, payroll: Payroll): Observable<Payroll> {
    return this.http.put<Payroll>(`${this.apiUrl}/${id}`, payroll);
  }
  deletePayroll(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  search(keyword: string): Observable<Payroll[]> {
    return this.http.get<Payroll[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
  generatePayroll(employeeId: number, year: number, month: number): Observable<Payroll> {
    return this.http.post<Payroll>(`${this.apiUrl}/generate/${employeeId}/${year}/${month}`, {});
  }
  payPayroll(id: number): Observable<Payroll> {
    return this.http.put<Payroll>(`${this.apiUrl}/${id}/pay`, {});
  }
  getMonthlyCostTrend(year: number): Observable<{
    [month: number]: number;
  }> {
    return this.http.get<{
      [month: number]: number;
    }>(`${this.apiUrl}/stats/monthly-cost?year=${year}`);
  }
}
