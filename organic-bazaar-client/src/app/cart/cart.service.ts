import { Injectable } from '@angular/core';
import { Product } from '../products/products.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cart: Map<number, CartItem> = new Map();
  cartSubject = new BehaviorSubject<Map<number, CartItem>>(new Map());

  constructor() { }

  addProduct(product: Product): void {
    if (this.cart.has(product.id)) {
      (this.cart.get(product.id) as CartItem).quantity++;
    } else {
      this.cart.set(product.id, { product, quantity: 1 });
    }

    this.cartSubject.next(this.cart);
  }

  removeProduct(productId: number): void {
    this.cart.delete(productId);
    this.cartSubject.next(this.cart);
  }

  incrementQuantity(productId: number): void {
    if (!this.cart.has(productId)) {
      console.error("Attempting to mutate non existent product in cart")
      return;
    }
    (this.cart.get(productId) as CartItem).quantity++;
    this.cartSubject.next(this.cart);
  }

  decrementQuantity(productId: number): void {
    if (!this.cart.has(productId)) {
      console.error("Attempting to mutate non existent product in cart")
      return;
    }
    (this.cart.get(productId) as CartItem).quantity--;
    if (this.cart.get(productId)?.quantity == 0) {
      this.removeProduct(productId);
    } else {
      this.cartSubject.next(this.cart);
    }
  }

  getTotalPrice(): number {
    let total = 0;
    for (let cartItem of this.cart.values()) {
      total += cartItem.quantity * cartItem.product.price;
    }
    return total;
  }
}

export interface CartItem {
  product: Product;
  quantity: number;
}
