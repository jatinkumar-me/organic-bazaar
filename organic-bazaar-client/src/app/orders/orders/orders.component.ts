import { Component, OnInit } from '@angular/core';
import { Order, OrdersService } from '../orders.service';
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
}
