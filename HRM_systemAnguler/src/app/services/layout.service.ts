import { Injectable, signal } from '@angular/core';
/** Shared chrome state: the header's hamburger collapses the sidebar. */
@Injectable({
  providedIn: 'root',
})
export class LayoutService {
  readonly sidebarCollapsed = signal(false);
  toggleSidebar(): void {
    this.sidebarCollapsed.update((v) => !v);
  }
  closeSidebar(): void {
    this.sidebarCollapsed.set(true);
  }
}
