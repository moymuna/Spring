import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { StorageService } from '../services/storage.service';
export const authGuard: CanActivateFn = () => {
  const storage = inject(StorageService);
  const router = inject(Router);
  if (storage.isLoggedIn()) return true;
  router.navigate(['/login']);
  return false;
};
export const roleGuard = (allowedRoles: string[]): CanActivateFn => {
  return () => {
    const storage = inject(StorageService);
    const router = inject(Router);
    const role = storage.getRole();
    if (role && allowedRoles.includes(role)) return true;
    router.navigate(['/dashboard']);
    return false;
  };
};
