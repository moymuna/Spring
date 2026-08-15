import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter } from 'rxjs';
import { StorageService } from '../../../../services/storage.service';
import { AuthService } from '../../../../services/auth.service';
import { EmployeeService } from '../../../../services/employee.service';
import { UserService } from '../../../../services/user.service';
import { ProfileEventsService } from '../../../../services/profile-events.service';
import { LayoutService } from '../../../../services/layout.service';
import { NotificationService, NotificationModel } from '../../../../services/notification.service';
import { environment } from '../../../../../environments/environment';
import { silentContext } from '../../../../interceptors/error.interceptor';
const DEFAULT_AVATAR =
  'data:image/svg+xml;utf8,' +
  encodeURIComponent(
    '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40 40"><rect width="40" height="40" rx="20" fill="#0B1F44"/><circle cx="20" cy="16" r="7" fill="#fff"/><path d="M6 36c1.5-8 8-12 14-12s12.5 4 14 12" fill="#fff"/></svg>',
  );
/** Page titles that don't read well when derived from the URL segment. */
const TITLE_OVERRIDES: Record<string, string> = {
  'admin-dashboard': 'Admin Dashboard',
  'hr-dashboard': 'HR Dashboard',
  'manager-dashboard': 'Manager Dashboard',
  'employee-dashboard': 'Employee Dashboard',
  'applicant-dashboard': 'Applicant Dashboard',
  'my-profile': 'My Profile',
  'my-salary': 'My Salary',
  'salary-sheet': 'Salary Sheet',
  'audit-log': 'Audit Log',
  salarygrade: 'Salary Grade',
  leavetype: 'Leave Type',
  leavebalance: 'Leave Balance',
  performancereview: 'Performance Review',
  jobpost: 'Job Posts',
  policestation: 'Police Station',
  advance: 'Advance Requests',
  documents: 'Documents',
  notice: 'Notice Board',
};
const ROLE_LABELS: Record<string, string> = {
  ADMIN: 'Administrator',
  HR: 'HR Manager',
  MANAGER: 'Manager',
  EMPLOYEE: 'Employee',
  APPLICANT: 'Applicant',
};
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Header implements OnInit {
  user: any = null;
  photoUrl = DEFAULT_AVATAR;
  showDropdown = false;
  notifications: NotificationModel[] = [];
  unreadCount = 0;
  showNotifications = false;
  pageTitle = 'Dashboard';
  roleLabel = '';
  searchTerm = '';
  constructor(
    private storage: StorageService,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private notificationService: NotificationService,
    private employeeService: EmployeeService,
    private userService: UserService,
    private profileEvents: ProfileEventsService,
    private layout: LayoutService,
  ) {}
  ngOnInit(): void {
    this.roleLabel = ROLE_LABELS[this.storage.getRole() ?? ''] ?? '';
    this.setTitleFromUrl(this.router.url);
    this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe((e) => {
        this.setTitleFromUrl(e.urlAfterRedirects);
        this.cdr.markForCheck();
      });
    const session = this.storage.getUser();
    if (session) {
      this.user = session;
      this.loadUnreadCount();
      this.loadPhoto(session.id);
      this.userService.getUserById(session.id).subscribe((user) => {
        this.storage.updateUserSession({ fullName: user.fullName, email: user.email });
        this.user = this.storage.getUser();
        this.cdr.markForCheck();
      });
    }
    this.profileEvents.avatarChanged$.subscribe(() => {
      if (this.user) {
        this.loadPhoto(this.user.id);
      }
    });
    this.profileEvents.profileUpdated$.subscribe(() => {
      this.user = this.storage.getUser();
      this.cdr.markForCheck();
    });
  }
  /** First URL segment becomes the heading, e.g. /salary/edit/3 -> "Salary". */
  private setTitleFromUrl(url: string): void {
    const segment = url.split('?')[0].split('/').filter(Boolean)[0] ?? 'dashboard';
    this.pageTitle =
      TITLE_OVERRIDES[segment] ??
      segment
        .split('-')
        .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
        .join(' ');
  }
  toggleSidebar(): void {
    this.layout.toggleSidebar();
  }
  /** The topbar search looks people up — the most common lookup in an HR system. */
  search(): void {
    const term = this.searchTerm.trim();
    if (!term) {
      return;
    }
    this.router.navigate(['/employee'], { queryParams: { q: term } });
  }
  loadUnreadCount(): void {
    this.notificationService.unreadCount().subscribe((count) => {
      this.unreadCount = count;
      this.cdr.markForCheck();
    });
  }
  loadPhoto(userId: number): void {
    this.employeeService.getByUserId(userId, silentContext()).subscribe({
      next: (employee) => {
        this.photoUrl = employee.image
          ? this.storage.appendToken(`${environment.imgUrl}employee/${employee.image}`)
          : DEFAULT_AVATAR;
        // Prefer the job title over the raw role, as on the reference design.
        if (employee.designationTitle) {
          this.roleLabel = employee.designationTitle;
        }
        this.cdr.markForCheck();
      },
      error: () => {
        this.photoUrl = DEFAULT_AVATAR;
        this.cdr.markForCheck();
      },
    });
  }
  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) {
      this.notificationService.mine().subscribe((data) => {
        this.notifications = data;
        this.cdr.markForCheck();
      });
    }
  }
  markRead(notification: NotificationModel): void {
    if (notification.read) {
      return;
    }
    this.notificationService.markRead(notification.id).subscribe(() => {
      notification.read = true;
      this.cdr.markForCheck();
      this.loadUnreadCount();
    });
  }
  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }
  logout(): void {
    this.auth.logout();
  }
  goToDashboard(): void {
    const role = this.storage.getRole();
    const map: Record<string, string> = {
      ADMIN: '/admin-dashboard',
      HR: '/hr-dashboard',
      EMPLOYEE: '/employee-dashboard',
      MANAGER: '/manager-dashboard',
      APPLICANT: '/applicant-dashboard',
    };
    this.router.navigate([map[role ?? ''] ?? '/dashboard']);
  }
}
