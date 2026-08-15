import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Training } from '../models/traning.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class TrainingService {
  private apiUrl = `${environment.baseUrl}/api/training`;
  constructor(private http: HttpClient) {}
  createTraining(training: Training): Observable<Training> {
    return this.http.post<Training>(this.apiUrl, training);
  }
  getAllTraining(): Observable<Training[]> {
    return this.http.get<Training[]>(this.apiUrl);
  }
  getTrainingById(id: number): Observable<Training> {
    return this.http.get<Training>(`${this.apiUrl}/${id}`);
  }
  updateTraining(id: number, training: Training): Observable<Training> {
    return this.http.put<Training>(`${this.apiUrl}/${id}`, training);
  }
  deleteTraining(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
  approveTraining(id: number): Observable<Training> {
    return this.http.put<Training>(`${this.apiUrl}/${id}/approve`, {});
  }
  rejectTraining(id: number, reason: string): Observable<Training> {
    return this.http.put<Training>(
      `${this.apiUrl}/${id}/reject?reason=${encodeURIComponent(reason)}`,
      {},
    );
  }
  applyForTraining(id: number, employeeId: number): Observable<Training> {
    return this.http.put<Training>(`${this.apiUrl}/${id}/apply?employeeId=${employeeId}`, {});
  }
}
