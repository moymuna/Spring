import { Injectable } from '@angular/core';
import { CryptoUtil } from '../utils/crypto.util';
import { LoginResponse } from '../models/auth.model';
export const KEYS = {
  TOKEN: 'cm_token',
  REFRESH_TOKEN: 'cm_refresh_token',
  USER: 'cm_user',
  ADMIN: 'cm_admin',
  EMPLOYEE: 'cm_employee',
  HR: 'cm_hr',
  MANAGER: 'cm_manager',
  APPLICANT: 'cm_applicant',
};
@Injectable({
  providedIn: 'root',
})
export class StorageService {
  saveSession(data: LoginResponse): void {
    localStorage.setItem(KEYS.TOKEN, CryptoUtil.encrypt(data.token));
    if (data.refreshToken) {
      localStorage.setItem(KEYS.REFRESH_TOKEN, CryptoUtil.encrypt(data.refreshToken));
    }
    localStorage.setItem(KEYS.USER, CryptoUtil.encrypt(JSON.stringify(data)));
  }
  updateTokens(token: string, refreshToken?: string): void {
    localStorage.setItem(KEYS.TOKEN, CryptoUtil.encrypt(token));
    if (refreshToken) {
      localStorage.setItem(KEYS.REFRESH_TOKEN, CryptoUtil.encrypt(refreshToken));
    }
  }
  getToken(): string | null {
    const raw = localStorage.getItem(KEYS.TOKEN);
    return raw ? CryptoUtil.decrypt(raw) : null;
  }
  getRefreshToken(): string | null {
    const raw = localStorage.getItem(KEYS.REFRESH_TOKEN);
    return raw ? CryptoUtil.decrypt(raw) : null;
  }
  appendToken(url: string): string {
    const token = this.getToken();
    if (!token || !url) return url;
    const separator = url.includes('?') ? '&' : '?';
    return `${url}${separator}token=${encodeURIComponent(token)}`;
  }
  getUser(): LoginResponse | null {
    const raw = localStorage.getItem(KEYS.USER);
    if (!raw) return null;
    const json = CryptoUtil.decrypt(raw);
    try {
      return json ? JSON.parse(json) : null;
    } catch {
      return null;
    }
  }
  updateUserSession(patch: Partial<LoginResponse>): void {
    const current = this.getUser();
    if (!current) return;
    const updated = { ...current, ...patch };
    localStorage.setItem(KEYS.USER, CryptoUtil.encrypt(JSON.stringify(updated)));
  }
  getRole(): string | null {
    return this.getUser()?.role ?? null;
  }
  isLoggedIn(): boolean {
    return !!this.getToken();
  }
  clearSession(): void {
    Object.values(KEYS).forEach((k) => localStorage.removeItem(k));
  }
  saveData(key: string, data: any): void {
    localStorage.setItem(key, CryptoUtil.encrypt(JSON.stringify(data)));
  }
  getData<T>(key: string): T | null {
    const raw = localStorage.getItem(key);
    if (!raw) return null;
    try {
      const json = CryptoUtil.decrypt(raw);
      return json ? JSON.parse(json) : null;
    } catch {
      return null;
    }
  }
  removeData(key: string): void {
    localStorage.removeItem(key);
  }
}
