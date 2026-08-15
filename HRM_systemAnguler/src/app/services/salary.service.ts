import { Injectable } from '@angular/core';
import { Salary } from '../models/salary.model';
import { HttpClient, HttpContext } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class SalaryService {
  private apiUrl = `${environment.baseUrl}/api/salary`;
  constructor(private http: HttpClient) {}
  createSalary(salary: Salary): Observable<Salary> {
    return this.http.post<Salary>(this.apiUrl, salary);
  }
  getAllSalary(): Observable<Salary[]> {
    return this.http.get<Salary[]>(this.apiUrl);
  }
  getSalaryById(id: number): Observable<Salary> {
    return this.http.get<Salary>(`${this.apiUrl}/${id}`);
  }
  getSalaryByEmployee(employeeId: number, context?: HttpContext): Observable<Salary> {
    return this.http.get<Salary>(`${this.apiUrl}/employee/${employeeId}`, { context });
  }
  getSalaryHistoryByEmployee(employeeId: number, context?: HttpContext): Observable<Salary[]> {
    return this.http.get<Salary[]>(`${this.apiUrl}/employee/${employeeId}/history`, { context });
  }
  updateSalary(id: number, salary: Salary): Observable<Salary> {
    return this.http.put<Salary>(`${this.apiUrl}/${id}`, salary);
  }
  deleteSalary(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
