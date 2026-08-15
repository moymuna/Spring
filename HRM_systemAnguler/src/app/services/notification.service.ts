import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
export interface NotificationModel {
  id: number;
  message: string;
  relatedEntityType: string;
  relatedEntityId: number;
  read: boolean;
  createdAt: string;
}
@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private apiUrl = `${environment.baseUrl}/api/notifications`;
  constructor(private http: HttpClient) {}
  mine(): Observable<NotificationModel[]> {
    return this.http.get<NotificationModel[]>(`${this.apiUrl}/mine`);
  }
  unreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/mine/unread-count`);
  }
  markRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/read`, {});
  }
}
