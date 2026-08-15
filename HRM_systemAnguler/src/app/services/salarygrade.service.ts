import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SalaryGrade } from '../models/salarygrade.model';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class SalaryGradeService {
  private apiUrl = `${environment.baseUrl}/api/salary-grades`;
  constructor(private http: HttpClient) {}
  createGrade(grade: SalaryGrade): Observable<SalaryGrade> {
    return this.http.post<SalaryGrade>(this.apiUrl, grade);
  }
  getAllGrades(): Observable<SalaryGrade[]> {
    return this.http.get<SalaryGrade[]>(this.apiUrl);
  }
  getActiveGrades(): Observable<SalaryGrade[]> {
    return this.http.get<SalaryGrade[]>(`${this.apiUrl}/active`);
  }
  getGradeById(id: number): Observable<SalaryGrade> {
    return this.http.get<SalaryGrade>(`${this.apiUrl}/${id}`);
  }
  getGradeByNumber(gradeNumber: number): Observable<SalaryGrade> {
    return this.http.get<SalaryGrade>(`${this.apiUrl}/number/${gradeNumber}`);
  }
  updateGrade(id: number, grade: SalaryGrade): Observable<SalaryGrade> {
    return this.http.put<SalaryGrade>(`${this.apiUrl}/${id}`, grade);
  }
  deleteGrade(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' as 'json' });
  }
}
