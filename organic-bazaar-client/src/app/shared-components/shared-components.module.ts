import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CartModule } from '../cart/cart.module';

@NgModule({
  declarations: [
    NavbarComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    CartModule,
  ],
  exports: [
    NavbarComponent
  ]
})
export class SharedComponentsModule { }
