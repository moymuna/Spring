import { Component } from '@angular/core';
import { StorageService } from '../../../services/storage.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-roll-redirect',
  imports: [],
  templateUrl: './roll-redirect.html',
  styleUrl: './roll-redirect.css',
})
export class RollRedirect {
  constructor(
    private storage: StorageService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    const role = this.storage.getRole();
    console.log('User role:', role);
    const map: Record<string, string> = {
      ADMIN: '/admin-dashboard',
      HR: '/hr-dashboard',
      EMPLOYEE: '/employee-dashboard',
      MANAGER: '/manager-dashboard',
      APPLICANT: '/applicant-dashboard',
    };
    this.router.navigate([map[role ?? ''] ?? '/login']);
  }
}
