import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Notice } from '../models/notice.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class NoticeService {
  private apiUrl = `${environment.baseUrl}/api/notice`;
  constructor(private http: HttpClient) {}
  createNotice(notice: Notice): Observable<Notice> {
    return this.http.post<Notice>(this.apiUrl, notice);
  }
  getAllNotices(): Observable<Notice[]> {
    return this.http.get<Notice[]>(this.apiUrl);
  }
  getNoticeById(id: number): Observable<Notice> {
    return this.http.get<Notice>(`${this.apiUrl}/${id}`);
  }
  updateNotice(id: number, notice: Notice): Observable<Notice> {
    return this.http.put<Notice>(`${this.apiUrl}/${id}`, notice);
  }
  deleteNotice(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text',
    });
  }
}
