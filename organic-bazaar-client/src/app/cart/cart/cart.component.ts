import { Component, OnInit } from '@angular/core';
import { CartItem, CartService } from '../cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: Map<number, CartItem> = new Map();

  constructor(private cartService: CartService) { }

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
}
