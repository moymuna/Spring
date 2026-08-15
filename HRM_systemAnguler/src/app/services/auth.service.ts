import { Injectable } from '@angular/core';
import {
  ForgotPasswordRequest,
  LoginRequest,
  LoginResponse,
  ResetPasswordRequest,
} from '../models/auth.model';
import { Observable, tap, catchError, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { StorageService } from './storage.service';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl + 'auth';
  constructor(
    private http: HttpClient,
    private storage: StorageService,
    private router: Router,
  ) {}
  login(dto: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, dto)
      .pipe(tap((res) => this.storage.saveSession(res)));
  }
  register(dto: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register-applicant`, dto);
  }
  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.storage.getRefreshToken();
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/refresh-token`, { refreshToken })
      .pipe(tap((res) => this.storage.updateTokens(res.token, res.refreshToken)));
  }
  logout(): void {
    const refreshToken = this.storage.getRefreshToken();
    this.http
      .post(`${this.apiUrl}/logout`, { refreshToken }, { responseType: 'text' })
      .pipe(catchError(() => of(null)))
      .subscribe(() => {
        this.storage.clearSession();
        this.router.navigate(['/login']);
      });
  }
  forgotPassword(dto: ForgotPasswordRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/forgot-password`, dto, { responseType: 'text' });
  }
  resetPassword(dto: ResetPasswordRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/reset-password`, dto, { responseType: 'text' });
  }
  verifyEmail(token: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/verify-email`, {
      params: { token },
      responseType: 'text',
    });
  }
  isLoggedIn(): boolean {
    return this.storage.isLoggedIn();
  }
  getRole(): string | null {
    return this.storage.getRole();
  }
  getUser(): LoginResponse | null {
    return this.storage.getUser();
  }
}
