import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APIResponse, Organisation } from '../../core/models/organisation.model';

@Injectable({ providedIn: 'root' })
export class OrganisationService {

  private baseUrl = 'http://localhost:8080/api/organisations';

  constructor(private http: HttpClient) {}

  getMyOrganisation(): Observable<APIResponse<Organisation>> {
    return this.http.get<APIResponse<Organisation>>(`${this.baseUrl}`);
  }

  update(id: string, org: Organisation): Observable<APIResponse<Organisation>> {
    return this.http.put<APIResponse<Organisation>>(`${this.baseUrl}`, org);
  }
}