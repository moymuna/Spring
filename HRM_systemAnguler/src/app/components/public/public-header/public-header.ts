import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
@Component({
  selector: 'app-public-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './public-header.html',
  styleUrl: './public-header.css',
})
export class PublicHeader {
  menuOpen = false;
  isLoginPage = false;
  isRegisterPage = false;
  constructor(private router: Router) {
    this.isLoginPage = this.router.url.startsWith('/login');
    this.isRegisterPage = this.router.url.startsWith('/register');
  }
  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }
  closeMenu(): void {
    this.menuOpen = false;
  }
}
