import { Injectable } from '@angular/core';
import { environment } from '../environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  apiUrl = new URL('products', environment.apiUrl);

  constructor(private httpClient: HttpClient) { }

  getProducts(productRequestParams: ProductRequestParams): Observable<Product[]> {
    const getProductsUrl = new URL(this.apiUrl);
    for (let param in productRequestParams) {
      if (!productRequestParams[param as keyof ProductRequestParams]) {
        continue;
      }
      getProductsUrl.searchParams.set(param, productRequestParams[param as keyof ProductRequestParams].toString())
    }
    return this.httpClient.get<Product[]>(getProductsUrl.toString());
  }

  getProductByIds(productIds: Set<number>): Observable<Product[]> {
    let params = new HttpParams();
    productIds.forEach(id => {
      params = params.append('ids', id.toString());
    });

    return this.httpClient.get<Product[]>(`${this.apiUrl}/ids`, { params });
  }

  getCategories(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.apiUrl.toString() + "/categories");
  }
}

export interface Product {
  id: number,
  name: string,
  description: string,
  image: string,
  category: string,
  price: number
}

export interface ProductRequestParams {
  sort: string,
  order: SortOrder
  category: string,
  search: string
}

export enum SortOrder {
  INC = -1,
  DEC = 1
}
