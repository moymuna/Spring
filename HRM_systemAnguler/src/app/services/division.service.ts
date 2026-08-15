import { Injectable } from '@angular/core';
import { DivisionModel } from '../models/division';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class DivisionService {
  private apiUrl = `${environment.baseUrl}/api/division`;
  constructor(private http: HttpClient) {}
  save(division: DivisionModel): Observable<DivisionModel> {
    return this.http.post<DivisionModel>(this.apiUrl, division);
  }
  getAll(): Observable<DivisionModel[]> {
    return this.http.get<DivisionModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<DivisionModel> {
    return this.http.get<DivisionModel>(`${this.apiUrl}/${id}`);
  }
  update(id: number, division: DivisionModel): Observable<DivisionModel> {
    return this.http.put<DivisionModel>(`${this.apiUrl}/${id}`, division);
  }
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
  getByCountryId(countryId: number): Observable<DivisionModel[]> {
    return this.http.get<DivisionModel[]>(`${this.apiUrl}/country/${countryId}`);
  }
}
