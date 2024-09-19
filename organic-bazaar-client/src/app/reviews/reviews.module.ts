import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewFormComponent } from './review-form/review-form.component';
import { ReviewComponent } from './review/review.component';



@NgModule({
  declarations: [
    ReviewFormComponent,
    ReviewComponent
  ],
  imports: [
    CommonModule
  ]
})
export class ReviewsModule { }
