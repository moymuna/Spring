import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DepartmentModel } from '../models/department.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class DepartmentService {
  private apiUrl = `${environment.baseUrl}/api/department`;
  constructor(private http: HttpClient) {}
  save(department: DepartmentModel): Observable<DepartmentModel> {
    return this.http.post<DepartmentModel>(this.apiUrl, department);
  }
  getAll(): Observable<DepartmentModel[]> {
    return this.http.get<DepartmentModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<DepartmentModel> {
    return this.http.get<DepartmentModel>(`${this.apiUrl}/${id}`);
  }
  update(id: number, department: DepartmentModel): Observable<DepartmentModel> {
    return this.http.put<DepartmentModel>(`${this.apiUrl}/${id}`, department);
  }
  delete(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
  getByName(name: string): Observable<DepartmentModel> {
    return this.http.get<DepartmentModel>(`${this.apiUrl}/name/${name}`);
  }
  getByCode(code: string): Observable<DepartmentModel> {
    return this.http.get<DepartmentModel>(`${this.apiUrl}/code/${code}`);
  }
  search(keyword: string): Observable<DepartmentModel[]> {
    return this.http.get<DepartmentModel[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
