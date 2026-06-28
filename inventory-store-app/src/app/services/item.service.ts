import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../models/item.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  constructor(private http: HttpClient) {}

  private getUrl(orgId: string): string {
    return `/api/organisations/${orgId}/items`;
  }

  getAll(orgId: string): Observable<ApiResponse<Item[]>> {
    return this.http.get<ApiResponse<Item[]>>(`${this.getUrl(orgId)}/all`);
  }

  getById(orgId: string, itemId: string): Observable<ApiResponse<Item>> {
    return this.http.get<ApiResponse<Item>>(`${this.getUrl(orgId)}/${itemId}`);
  }

  create(orgId: string, item: Item): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(this.getUrl(orgId), item);
  }

  update(orgId: string, itemId: string, item: Item): Observable<ApiResponse<Item>> {
    return this.http.put<ApiResponse<Item>>(`${this.getUrl(orgId)}/${itemId}`, item);
  }

  delete(orgId: string, itemId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.getUrl(orgId)}/${itemId}`);
  }
}
