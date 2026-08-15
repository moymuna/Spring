import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DesignationModel } from '../models/designation.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class DesignationService {
  private apiUrl = `${environment.baseUrl}/api/designation`;
  constructor(private http: HttpClient) {}
  save(designation: DesignationModel): Observable<DesignationModel> {
    return this.http.post<DesignationModel>(this.apiUrl, designation);
  }
  getAll(): Observable<DesignationModel[]> {
    return this.http.get<DesignationModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<DesignationModel> {
    return this.http.get<DesignationModel>(`${this.apiUrl}/${id}`);
  }
  update(id: number, designation: DesignationModel): Observable<DesignationModel> {
    return this.http.put<DesignationModel>(`${this.apiUrl}/${id}`, designation);
  }
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
  getByDepartment(departmentId: number): Observable<DesignationModel[]> {
    return this.http.get<DesignationModel[]>(`${this.apiUrl}/department/${departmentId}`);
  }
  search(keyword: string): Observable<DesignationModel[]> {
    return this.http.get<DesignationModel[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
