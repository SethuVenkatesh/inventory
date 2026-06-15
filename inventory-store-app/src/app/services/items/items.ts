import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../../core/models/item.model';
import { APIResponse } from '../../core/models/organisation.model';

@Injectable({ providedIn: 'root' })
export class ItemService {

  private baseUrl = 'http://localhost:8080/api/items';

  constructor(private http: HttpClient) {}

  getAll(): Observable<APIResponse<Item[]>> {
    return this.http.get<APIResponse<Item[]>>(`${this.baseUrl}/all`);
  }

  getById(id: string): Observable<APIResponse<Item>> {
    return this.http.get<APIResponse<Item>>(`${this.baseUrl}`,{ params: { itemId: id } });
  }

  create(item: Item): Observable<APIResponse<Item>> {
    return this.http.post<APIResponse<Item>>(this.baseUrl, item);
  }

  update(id: string, item: Item): Observable<APIResponse<Item>> {
    return this.http.put<APIResponse<Item>>(`${this.baseUrl}`, item, {
      params: { itemId: id }
    });
  }


  delete(id: string): Observable<APIResponse<void>> {
    return this.http.delete<APIResponse<void>>(`${this.baseUrl}`,{
      params: { itemId: id }
    });
  }
}