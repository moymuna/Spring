import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DistrictModel } from '../models/district';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class DistrictService {
  private baseUrl = `${environment.baseUrl}/api/district`;
  constructor(private http: HttpClient) {}
  getAll(): Observable<DistrictModel[]> {
    return this.http.get<DistrictModel[]>(this.baseUrl);
  }
  getById(id: number): Observable<DistrictModel> {
    return this.http.get<DistrictModel>(`${this.baseUrl}/${id}`);
  }
  save(data: DistrictModel): Observable<DistrictModel> {
    return this.http.post<DistrictModel>(this.baseUrl, data);
  }
  update(id: number, data: DistrictModel): Observable<DistrictModel> {
    return this.http.put<DistrictModel>(`${this.baseUrl}/${id}`, data);
  }
  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
  getByDivisionId(divisionId: number): Observable<DistrictModel[]> {
    return this.http.get<DistrictModel[]>(`${this.baseUrl}/division/${divisionId}`);
  }
  getByDivisionName(divisionName: string): Observable<DistrictModel[]> {
    return this.http.get<DistrictModel[]>(`${this.baseUrl}/division-name/${divisionName}`);
  }
}
