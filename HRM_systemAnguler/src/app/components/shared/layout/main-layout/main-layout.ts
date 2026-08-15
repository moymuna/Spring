import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Sidebar } from '../sidebar/sidebar';
import { Header } from '../header/header';
import { RouterOutlet } from '@angular/router';
import { LayoutService } from '../../../../services/layout.service';
@Component({
  selector: 'app-main-layout',
  imports: [CommonModule, Sidebar, Header, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  /** Shifts the content across when the sidebar collapses to its icon rail. */
  readonly collapsed = inject(LayoutService).sidebarCollapsed;
}
