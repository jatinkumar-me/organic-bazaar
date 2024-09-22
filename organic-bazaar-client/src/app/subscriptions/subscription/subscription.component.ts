import { Component } from '@angular/core';
import { Subscription, SubscriptionService, SubscriptionStatus } from '../subscription.service';
import { Product, ProductsService } from '../../products/products.service';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrl: './subscription.component.css'
})
export class SubscriptionComponent {
  subscriptions: Subscription[] = [];
  products: Map<number, Product> = new Map();

  constructor(private subscriptionService: SubscriptionService, private productsService: ProductsService) { }

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  loadSubscriptions() {
    this.subscriptionService.getUserSubscriptions().subscribe({
      next: (data) => {
        this.subscriptions = data;
        this.loadProductDetails();
      },
      error: (err) => {
        console.error("Error fetching subscriptions", err);
      }
    });
  }

  cancelSubscription(subscriptionId: number) {
    this.subscriptionService.changeSubscriptionStatus(subscriptionId, SubscriptionStatus.CANCELLED).subscribe({
      next: () => {
        alert("Cancelled subscription");
        this.loadSubscriptions();
      },
      error: (err) => {
        console.error("Error cancelling subscription", err);
      }
    });
  }

  pauseSubscription(subscriptionId: number) {
    this.subscriptionService.changeSubscriptionStatus(subscriptionId, SubscriptionStatus.PAUSED).subscribe({
      next: () => {
        this.loadSubscriptions();
      },
      error: (err) => {
        console.error("Error pausing subscription", err);
      }
    });
  }

  resumeSubscription(subscriptionId: number) {
    this.subscriptionService.changeSubscriptionStatus(subscriptionId, SubscriptionStatus.ACTIVE).subscribe({
      next: () => {
        this.loadSubscriptions();
      },
      error: (err) => {
        console.error("Error pausing subscription", err);
      }
    });
  }

  loadProductDetails() {
    const productIds: Set<number> = new Set();
    this.subscriptions.forEach(subscription => {
      productIds.add(subscription.productId);
    });
    this.productsService.getProductByIds(productIds).subscribe({
      next: (products) => {
        products.forEach(product => this.products.set(product.id, product));
      },
      error: (err) => console.error("Couldn't load product details", err),
    });
  }

  calculatePayable(subscription: Subscription): number {
    const product = this.products.get(subscription.productId);
    if (product) {
      return product.price * subscription.quantity;
    }
    return 0;
  }
}
