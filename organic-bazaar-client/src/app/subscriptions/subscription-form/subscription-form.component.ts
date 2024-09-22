import { Component, Input } from '@angular/core';
import { Product } from '../../products/products.service';
import { DayOfWeek, DeliveryScheduleType, SubscriptionRequest, SubscriptionService } from '../subscription.service';

@Component({
  selector: 'app-subscription-form',
  templateUrl: './subscription-form.component.html',
  styleUrl: './subscription-form.component.css'
})
export class SubscriptionFormComponent {
  @Input() product!: Product | null;

  subscriptionRequest: SubscriptionRequest = {
    productId: 0,
    quantity: 1,
    scheduleType: DeliveryScheduleType.WEEKLY,
    deliveryDays: [],
    dayOfMonth: undefined,
    startDate: undefined,
    endDate: undefined,
  };

  deliveryScheduleType = DeliveryScheduleType;
  daysOfWeek = Object.values(DayOfWeek);

  constructor(private subscriptionService: SubscriptionService) { }

  createSubscription() {
    if (!this.product) return;
    this.subscriptionRequest.productId = this.product.id;

    this.subscriptionService.createSubscription(this.subscriptionRequest).subscribe({
      next: () => {
        alert('Subscription created successfully');
        let closeBtn = document.querySelector('.btn-close') as HTMLButtonElement;
        closeBtn?.click();
      },
      error: (error) => {
        console.error('Error creating subscription', error);
      },
    });
  }

  incrementQuantity() {
    this.subscriptionRequest.quantity++;
  }

  decrementQuantity() {
    if (this.subscriptionRequest.quantity > 1) {
      this.subscriptionRequest.quantity--;
    }
  }

  calculatePayableAmount(): number {
    if (!this.product) return 0;
    return this.subscriptionRequest.quantity * this.product.price;
  }

  toggleDay(day: DayOfWeek) {
    console.log(this.subscriptionRequest);
    if (!this.subscriptionRequest.deliveryDays) {
      this.subscriptionRequest.deliveryDays = [];
    }
    const index = this.subscriptionRequest.deliveryDays.indexOf(day);
    if (index === -1) {
      this.subscriptionRequest.deliveryDays.push(day);
    } else {
      this.subscriptionRequest.deliveryDays.splice(index, 1);
    }
    if (this.subscriptionRequest.deliveryDays.length == 0) {
      delete this.subscriptionRequest.deliveryDays;
    }
  }

  resetDeliveryDays() {
    this.subscriptionRequest.deliveryDays = [];
  }
}
