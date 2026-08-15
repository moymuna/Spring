import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InterviewModel } from '../models/interview.model';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class InterviewService {
  private apiUrl = `${environment.baseUrl}/api/interviews`;
  constructor(private http: HttpClient) {}
  schedule(data: InterviewModel): Observable<InterviewModel> {
    return this.http.post<InterviewModel>(`${this.apiUrl}/schedule`, data);
  }
  getAll(): Observable<InterviewModel[]> {
    return this.http.get<InterviewModel[]>(this.apiUrl);
  }
  getById(id: number): Observable<InterviewModel> {
    return this.http.get<InterviewModel>(`${this.apiUrl}/${id}`);
  }
  getByApplication(applicationId: number): Observable<InterviewModel[]> {
    return this.http.get<InterviewModel[]>(`${this.apiUrl}/application/${applicationId}`);
  }
  getByInterviewer(interviewerId: number): Observable<InterviewModel[]> {
    return this.http.get<InterviewModel[]>(`${this.apiUrl}/interviewer/${interviewerId}`);
  }
  update(id: number, data: InterviewModel): Observable<InterviewModel> {
    return this.http.put<InterviewModel>(`${this.apiUrl}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
