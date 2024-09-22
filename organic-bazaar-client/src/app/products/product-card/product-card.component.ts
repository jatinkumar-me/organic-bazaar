import { Component, Input } from '@angular/core';
import { Product } from '../products.service';
import { CartService } from '../../cart/cart.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.css'
})
export class ProductCardComponent {
  @Input() product: Product | null = null;
  showModal: boolean = false;

  constructor(private cartService: CartService) { }

  addProductToCart() {
    if (this.product == null) {
      return;
    }
    this.cartService.addProduct(this.product);
  }

  closeModal() {
    this.showModal = false;
  }

  openModal() {
    this.showModal = true;
  }
}
