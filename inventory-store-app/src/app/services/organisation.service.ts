import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Organisation } from '../models/organisation.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class OrganisationService {
  private baseUrl = '/api/organisations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<Organisation[]>> {
    return this.http.get<ApiResponse<Organisation[]>>(`${this.baseUrl}/all`);
  }

  getById(id: string): Observable<ApiResponse<Organisation>> {
    return this.http.get<ApiResponse<Organisation>>(`${this.baseUrl}/${id}`);
  }

  create(org: Organisation): Observable<ApiResponse<Organisation>> {
    return this.http.post<ApiResponse<Organisation>>(this.baseUrl, org);
  }

  update(id: string, org: Organisation): Observable<ApiResponse<Organisation>> {
    return this.http.put<ApiResponse<Organisation>>(`${this.baseUrl}/${id}`, org);
  }

  delete(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}
