import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PerformanceReview } from '../models/performancereview.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class PerformancereviewService {
  private apiUrl = `${environment.baseUrl}/api/performancereview`;
  constructor(private http: HttpClient) {}
  createPerformanceReview(review: PerformanceReview): Observable<PerformanceReview> {
    return this.http.post<PerformanceReview>(this.apiUrl, review);
  }
  getAllPerformanceReviews(): Observable<PerformanceReview[]> {
    return this.http.get<PerformanceReview[]>(this.apiUrl);
  }
  getPerformanceReviewById(id: number): Observable<PerformanceReview> {
    return this.http.get<PerformanceReview>(`${this.apiUrl}/${id}`);
  }
  updatePerformanceReview(id: number, review: PerformanceReview): Observable<PerformanceReview> {
    return this.http.put<PerformanceReview>(`${this.apiUrl}/${id}`, review);
  }
  deletePerformanceReview(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
}
