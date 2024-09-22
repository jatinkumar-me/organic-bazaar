import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { ProductCardComponent } from './product-card/product-card.component';
import { FormsModule } from '@angular/forms';
import { ReviewsListComponent } from './reviews-list/reviews-list.component';
import { RouterModule } from '@angular/router';
import { SubscriptionsModule } from '../subscriptions/subscriptions.module';



@NgModule({
  declarations: [
    ProductListComponent,
    ProductDetailsComponent,
    ProductCardComponent,
    ReviewsListComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SubscriptionsModule,
  ]
})
export class ProductsModule { }
