import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Holiday } from '../models/holiday.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class HolidayService {
  private apiUrl = `${environment.baseUrl}/api/holiday`;
  constructor(private http: HttpClient) {}
  saveHoliday(holiday: Holiday): Observable<Holiday> {
    return this.http.post<Holiday>(`${this.apiUrl}/save`, holiday);
  }
  getAllHolidays(): Observable<Holiday[]> {
    return this.http.get<Holiday[]>(`${this.apiUrl}/all`);
  }
  getUpcomingHolidays(limit = 5): Observable<Holiday[]> {
    return this.http.get<Holiday[]>(`${this.apiUrl}/upcoming?limit=${limit}`);
  }
  getHolidayById(id: number): Observable<Holiday> {
    return this.http.get<Holiday>(`${this.apiUrl}/${id}`);
  }
  updateHoliday(id: number, holiday: Holiday): Observable<Holiday> {
    return this.http.put<Holiday>(`${this.apiUrl}/update/${id}`, holiday);
  }
  deleteHoliday(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, {
      responseType: 'text',
    });
  }
}
