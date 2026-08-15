import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OfficeRequest, OfficeResponse } from '../models/office.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class OfficeService {
  private apiUrl = `${environment.baseUrl}/api/office`;
  constructor(private http: HttpClient) {}
  createOffice(office: OfficeRequest): Observable<OfficeResponse> {
    return this.http.post<OfficeResponse>(this.apiUrl, office);
  }
  getAllOffice(): Observable<OfficeResponse[]> {
    return this.http.get<OfficeResponse[]>(this.apiUrl);
  }
  getOfficeById(id: number): Observable<OfficeResponse> {
    return this.http.get<OfficeResponse>(`${this.apiUrl}/${id}`);
  }
  updateOffice(id: number, office: OfficeRequest): Observable<OfficeResponse> {
    return this.http.put<OfficeResponse>(`${this.apiUrl}/${id}`, office);
  }
  deleteOffice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  search(keyword: string): Observable<OfficeResponse[]> {
    return this.http.get<OfficeResponse[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
