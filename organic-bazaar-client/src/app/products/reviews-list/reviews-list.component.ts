import { Component, Input, OnInit } from '@angular/core';
import { Product, ProductsService, ReviewRequest } from '../products.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-reviews-list',
  templateUrl: './reviews-list.component.html',
  styleUrl: './reviews-list.component.css'
})
export class ReviewsListComponent implements OnInit {
  @Input() product!: Product;
  newReview: ReviewRequest = { comment: '', rating: 0 };
  isLoggedIn: boolean = false;
  currentUserId: number | null = null;

  constructor(private authService: AuthService, private productService: ProductsService) { }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      this.isLoggedIn = user != null;
      this.currentUserId = user ? user.userId : null;
    });
  }

  postReview() {
    if (this.isLoggedIn && this.product.id) {
      this.productService.postReview(this.product.id, this.newReview).subscribe({
        next: (review) => {
          this.product.reviews.push(review);
          this.newReview = { comment: '', rating: 0 };
        },
        error: (err) => console.error("Error posting review:", err)
      });
    }
  }

  deleteReview(reviewId: number) {
    if (this.isLoggedIn && this.product.id) {
      this.productService.delete(this.product.id, reviewId).subscribe({
        next: () => {
          this.product.reviews = this.product.reviews.filter(review => review.id !== reviewId);
        },
        error: (err) => console.error("Error deleting review:", err)
      });
    }
  }
}
