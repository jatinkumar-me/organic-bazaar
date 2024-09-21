import { Injectable } from '@angular/core';
import { environment } from '../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {
  private apiUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) { }

  createOrder(orderRequest: OrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, orderRequest);
  }

  cancelOrder(orderId: number): Observable<Order> {
    return this.http.patch<Order>(`${this.apiUrl}/${orderId}/cancel`, null);
  }

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  getOrderById(orderId: number): Observable<Order> {
    const url = `${this.apiUrl}/${orderId}`;
    return this.http.get<Order>(url);
  }

  deleteOrder(orderId: number): Observable<void> {
    const url = `${this.apiUrl}/${orderId}`;
    return this.http.delete<void>(url);
  }
}

export interface OrderItemRequest {
  productId: number,
  quantity: number,
}

export interface OrderItem extends OrderItemRequest {
  id: number,
}

export interface OrderRequest {
  orderItems: OrderItemRequest[];
}

export interface Order extends OrderRequest {
  id: number,
  userId: number,
  orderDate: Date,
  orderStatus: OrderStatus,
  totalPrice: number,
  subscriptionId: number | null,
}

export enum OrderStatus {
  PENDING = "PENDING",
  SHIPPED = "SHIPPED",
  DELIVERED = "DELIVERED",
  CANCELLED = "CANCELLED"
}
