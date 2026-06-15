import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth/auth';
import { AuthResponse } from '../../../core/models/auth.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="nav-brand">📦 Inventory</div>
      <div class="nav-links">
        <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
        <a routerLink="/items" routerLinkActive="active">Items</a>
        <a routerLink="/organisation" routerLinkActive="active">Organisation</a>
      </div>
      <div class="nav-user">
        <span>{{ user?.name }}</span>
        <button (click)="logout()">Logout</button>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex; align-items: center; justify-content: space-between;
      padding: 0 2rem; height: 60px;
      background: #1a1a2e; color: white;
      box-shadow: 0 2px 8px rgba(0,0,0,0.2);
    }
    .nav-brand { font-size: 1.2rem; font-weight: 700; }
    .nav-links a {
      color: #a0aec0; text-decoration: none;
      margin-left: 1.5rem; font-size: 0.9rem;
      &.active { color: white; font-weight: 600; }
      &:hover { color: white; }
    }
    .nav-user { display: flex; align-items: center; gap: 1rem; font-size: 0.9rem; }
    .nav-user button {
      padding: 0.4rem 0.9rem; background: #e53e3e;
      color: white; border: none; border-radius: 6px;
      cursor: pointer; font-size: 0.85rem;
    }
  `]
})
export class NavbarComponent {
  user: AuthResponse | null = null;
  constructor(private authService: AuthService) {
    this.user = this.authService.getCurrentUser();
  }
  logout() { this.authService.logout(); }
}