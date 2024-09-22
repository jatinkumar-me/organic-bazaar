import { Component, OnInit } from '@angular/core';
import { Order, OrdersService, OrderStatus } from '../orders.service';
import { Product, ProductsService } from '../../products/products.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {

  orders: Order[] = [];
  products: Map<number, Product> = new Map();

  constructor(private ordersService: OrdersService, private productService: ProductsService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.ordersService.getAllOrders().subscribe((data: Order[]) => {
      this.orders = data;
      this.fetchProductsData();
    });
  }

  fetchProductsData() {
    const productIds: Set<number> = new Set();
    this.orders.forEach(order => {
      order.orderItems.forEach(orderItem => {
        productIds.add(orderItem.productId);
      })
    });
    this.productService.getProductByIds(productIds).subscribe({
      next: (data) => {
        data.forEach(product => {
          this.products.set(product.id, product);
        });
      },
      error: (err) => {
        console.error("error fetching products data", err);
      }
    });
  }

  cancelOrder(orderId: number) {
    this.ordersService.cancelOrder(orderId).subscribe({
      next: () => {
        alert("Order cancelled")
        this.orders[this.orders.findIndex((order) => order.id === orderId)].status = OrderStatus.CANCELLED;
      },
      error: (err) => {
        console.error("error cancelling order", err);
      }
    });
  }
}
