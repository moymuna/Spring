import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PoliceStationModel } from '../models/policestation.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class PolicestationService {
  private apiUrl = `${environment.baseUrl}/api/policeStation`;
  constructor(private http: HttpClient) {}
  save(station: PoliceStationModel): Observable<PoliceStationModel> {
    return this.http.post<PoliceStationModel>(this.apiUrl, station);
  }
  getAll(): Observable<PoliceStationModel[]> {
    return this.http.get<PoliceStationModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<PoliceStationModel> {
    return this.http.get<PoliceStationModel>(`${this.apiUrl}/${id}`);
  }
  update(id: number, station: PoliceStationModel): Observable<PoliceStationModel> {
    return this.http.put<PoliceStationModel>(`${this.apiUrl}/${id}`, station);
  }
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
  getByDistrictId(districtId: number): Observable<PoliceStationModel[]> {
    return this.http.get<PoliceStationModel[]>(`${this.apiUrl}/district/${districtId}`);
  }
}
