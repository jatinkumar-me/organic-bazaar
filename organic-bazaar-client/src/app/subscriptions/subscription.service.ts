import { Injectable } from '@angular/core';
import { environment } from '../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiUrl = `${environment.apiUrl}/subscriptions`;

  constructor(private httpClient: HttpClient) {}

  getUserSubscriptions(): Observable<Subscription[]> {
    return this.httpClient.get<Subscription[]>(this.apiUrl);
  }

  createSubscription(subscriptionRequest: SubscriptionRequest): Observable<Subscription> {
    return this.httpClient.post<Subscription>(this.apiUrl, subscriptionRequest);
  }

  changeSubscriptionStatus(subscriptionId: number, subscriptionStatus: SubscriptionStatus): Observable<Subscription> {
    return this.httpClient.patch<Subscription>(`${this.apiUrl}/${subscriptionId}/status`, { status: subscriptionStatus });
  }
}

export interface SubscriptionRequest {
  productId: number,
  quantity: number,
	startDate?: Date,
	endDate?: Date
  scheduleType: DeliveryScheduleType,
  deliveryDays?: DayOfWeek[],
  dayOfMonth?: number,
}

export interface Subscription extends SubscriptionRequest {
  id: number,
  userId: number,
  status: SubscriptionStatus
  nextDeliveryDate: Date,
}

export enum DeliveryScheduleType {
  WEEKLY = "WEEKLY",
  MONTHLY = "MONTHLY"
}

export enum DayOfWeek {
  MONDAY = "MONDAY",
  TUESDAY = "TUESDAY",
  WEDNESDAY = "WEDNESDAY",
  THURSDAY = "THURSDAY",
  FRIDAY = "FRIDAY",
  SATURDAY = "SATURDAY",
  SUNDAY = "SUNDAY"
}

export enum SubscriptionStatus {
	ACTIVE = "ACTIVE",
	PAUSED = "PAUSED",
	CANCELLED = "CANCELLED",
	EXPIRED = "EXPIRED"
}

export interface SubscriptionStatusRequest {
  status: SubscriptionStatus,
  pauseTill?: Date,
}
