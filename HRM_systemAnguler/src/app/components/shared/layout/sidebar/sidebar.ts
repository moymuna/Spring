import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { StorageService } from '../../../../services/storage.service';
import { LayoutService } from '../../../../services/layout.service';
@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Sidebar {
  role: string | null;
  /** Collapsed by the header's hamburger; a signal so OnPush picks it up. */
  readonly collapsed;
  constructor(
    private storage: StorageService,
    private layout: LayoutService,
  ) {
    this.role = this.storage.getRole();
    this.collapsed = this.layout.sidebarCollapsed;
  }
  canSee(allowedRoles: string[]): boolean {
    return !!this.role && allowedRoles.includes(this.role);
  }
}
