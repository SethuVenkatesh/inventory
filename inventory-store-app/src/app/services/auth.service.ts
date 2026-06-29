import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginRequest, RegisterRequest, AuthResponse, User } from '../models/auth.model';
import { ApiResponse } from '../models/api-response.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private activeOrganisationIdSubject = new BehaviorSubject<string | null>(null);
  public activeOrganisationId$ = this.activeOrganisationIdSubject.asObservable();

  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';
  private readonly ORG_KEY = 'active_org_id';

  constructor(private http: HttpClient) {
    // Restore session on init
    const token = localStorage.getItem(this.TOKEN_KEY);
    const userJson = localStorage.getItem(this.USER_KEY);
    const orgId = localStorage.getItem(this.ORG_KEY);

    if (token && userJson) {
      try {
        this.currentUserSubject.next(JSON.parse(userJson));
      } catch (e) {
        this.logout();
      }
    }
    if (orgId) {
      this.activeOrganisationIdSubject.next(orgId);
    }
  }

  public get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  public get activeOrganisationId(): string | null {
    return this.activeOrganisationIdSubject.value;
  }

  public setOrganisationId(orgId: string | null): void {
    if (orgId) {
      localStorage.setItem(this.ORG_KEY, orgId);
    } else {
      localStorage.removeItem(this.ORG_KEY);
    }
    this.activeOrganisationIdSubject.next(orgId);
  }

  login(credentials: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${environment.apiBase}/auth/login`, credentials).pipe(
      map(response => {
        if (response.success && response.data) {
          const authData = response.data;
          localStorage.setItem(this.TOKEN_KEY, authData.token);
          
          const user: User = {
            name: authData.name,
            email: authData.email,
            role: authData.role
          };
          localStorage.setItem(this.USER_KEY, JSON.stringify(user));
          this.currentUserSubject.next(user);

          // Save the organization ID returned
          if (authData.organisationId) {
            this.setOrganisationId(authData.organisationId);
          }
        }
        return response;
      })
    );
  }

  register(regRequest: RegisterRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${environment.apiBase}/auth/register`, regRequest).pipe(
      map(response => {
        if (response.success && response.data) {
          const authData = response.data;
          localStorage.setItem(this.TOKEN_KEY, authData.token);
          
          const user: User = {
            name: authData.name,
            email: authData.email,
            role: authData.role
          };
          localStorage.setItem(this.USER_KEY, JSON.stringify(user));
          this.currentUserSubject.next(user);

          if (authData.organisationId) {
            this.setOrganisationId(authData.organisationId);
          }
        }
        return response;
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.ORG_KEY);
    this.currentUserSubject.next(null);
    this.activeOrganisationIdSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
