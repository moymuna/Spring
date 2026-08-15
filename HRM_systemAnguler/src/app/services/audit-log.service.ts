import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
export interface AuditLogEntry {
  id: number;
  entityType: string;
  entityId: number;
  action: string;
  actorEmail: string;
  actorRole: string;
  details: string;
  timestamp: string;
}
@Injectable({
  providedIn: 'root',
})
export class AuditLogService {
  private apiUrl = `${environment.baseUrl}/api/audit-log`;
  constructor(private http: HttpClient) {}
  getLatest(): Observable<AuditLogEntry[]> {
    return this.http.get<AuditLogEntry[]>(`${this.apiUrl}/latest`);
  }
  getByPage(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/page?page=${page}&size=${size}`);
  }
}
