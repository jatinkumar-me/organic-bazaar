import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { CartService } from '../../cart/cart.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;
  username: string | undefined;
  cartItemCount = 0;
  showModal = false;

  constructor(private authService: AuthService, private cartService: CartService) { }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      this.isLoggedIn = user != null;
      this.username = user?.name;
    });
    this.cartService.cartSubject.subscribe(cart => {
      this.cartItemCount = cart.size;
    });
  }

  openCart(): void {
    this.showModal = true;
  }

  closeCart(): void {
    this.showModal = false;
  }

  confirmLogout(): void {
    const confirmation = window.confirm('Are you sure you want to logout?');
    if (confirmation) {
      this.logout();
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
