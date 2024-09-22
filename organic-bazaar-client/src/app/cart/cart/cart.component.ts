import { Component, OnInit } from '@angular/core';
import { CartItem, CartService } from '../cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: Map<number, CartItem> = new Map();

  constructor(private cartService: CartService, private router: Router) { }

  ngOnInit(): void {
    this.cartService.cartSubject.subscribe(cart => {
      this.cartItems = cart;
    });
  }

  increment(productId: number): void {
    this.cartService.incrementQuantity(productId);
  }

  decrement(productId: number): void {
    this.cartService.decrementQuantity(productId);
  }

  getTotalPrice(): number {
    return this.cartService.getTotalPrice();
  }

  removeProduct(productId: number): void {
    this.cartService.removeProduct(productId);
  }

  placeOrder() {
    this.cartService.makeOrder().subscribe({
      next: () => {
        alert("Order placed successfully")
        this.router.navigate(['/orders']);
      },
      error: (err) => { console.error("Couldn't create order, reason", err) },
    });
  }
}
