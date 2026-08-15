import {
  HttpContext,
  HttpContextToken,
  HttpErrorResponse,
  HttpInterceptorFn,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { StorageService } from '../services/storage.service';
import { ToastService } from '../services/toast.service';
import { AuthService } from '../services/auth.service';
export const SUPPRESS_ERROR_TOAST = new HttpContextToken<boolean>(() => false);
export function silentContext(): HttpContext {
  return new HttpContext().set(SUPPRESS_ERROR_TOAST, true);
}
const AUTH_ENDPOINTS = [
  '/auth/login',
  '/auth/signup',
  '/auth/register-applicant',
  '/auth/refresh-token',
  '/auth/forgot-password',
  '/auth/reset-password',
  '/auth/verify-email',
  '/auth/send-verification',
];
let isRefreshing = false;
const refreshedToken$ = new BehaviorSubject<string | null>(null);
function forceLogout(storage: StorageService, toast: ToastService, router: Router): void {
  storage.clearSession();
  toast.error('Your session has expired. Please log in again.');
  router.navigate(['/login']);
}
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const storage = inject(StorageService);
  const toast = inject(ToastService);
  const authService = inject(AuthService);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const serverMessage = error.error?.message || error.error;
      if (error.status === 401) {
        if (AUTH_ENDPOINTS.some((path) => req.url.includes(path))) {
          return throwError(() => error);
        }
        if (!storage.getRefreshToken()) {
          forceLogout(storage, toast, router);
          return throwError(() => error);
        }
        if (!isRefreshing) {
          isRefreshing = true;
          refreshedToken$.next(null);
          return authService.refreshToken().pipe(
            switchMap((res) => {
              isRefreshing = false;
              refreshedToken$.next(res.token);
              const retryReq = req.clone({ setHeaders: { Authorization: `Bearer ${res.token}` } });
              return next(retryReq);
            }),
            catchError(() => {
              isRefreshing = false;
              forceLogout(storage, toast, router);
              return throwError(() => error);
            }),
          );
        }
        return refreshedToken$.pipe(
          filter((token): token is string => token !== null),
          take(1),
          switchMap((token) => {
            const retryReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
            return next(retryReq);
          }),
        );
      }
      if (req.context.get(SUPPRESS_ERROR_TOAST)) {
        return throwError(() => error);
      }
      switch (error.status) {
        case 403:
          toast.error('You do not have permission to do that.');
          break;
        case 404:
          toast.error(
            typeof serverMessage === 'string' ? serverMessage : 'The requested item was not found.',
          );
          break;
        case 0:
          toast.error('Could not reach the server. Please check your connection.');
          break;
        default:
          if (error.status >= 500) {
            toast.error('Something went wrong on the server. Please try again.');
          } else if (typeof serverMessage === 'string' && serverMessage) {
            toast.error(serverMessage);
          }
      }
      return throwError(() => error);
    }),
  );
};
