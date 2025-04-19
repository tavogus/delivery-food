import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  restaurantId: number;
  name: string;
  description: string;
  price: number;
  category: string;
  imageUrl: string;
  available: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api'; // Base URL

  constructor(private http: HttpClient) { }

  getProductsByRestaurant(restaurantId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/products/restaurant/${restaurantId}`);
  }
}
