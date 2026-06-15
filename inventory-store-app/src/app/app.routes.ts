import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login')
      .then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register')
      .then(m => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard')
      .then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'items',
    loadComponent: () => import('./pages/items/items')
      .then(m => m.ItemsComponent),
    canActivate: [authGuard]
  },
  {
    path: 'organisation',
    loadComponent: () => import('./pages/organisation/organisation')
      .then(m => m.OrganisationComponent),
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: 'dashboard' }
];