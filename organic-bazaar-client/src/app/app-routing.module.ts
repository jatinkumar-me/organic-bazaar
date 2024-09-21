import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { RegisterComponent } from './auth/register/register.component';
import { ProductListComponent } from './products/product-list/product-list.component';
import { OrdersComponent } from './orders/orders/orders.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: 'products', component: ProductListComponent },
      { path: 'orders', component: OrdersComponent, canActivate: [AuthGuard] },
    ]
  },
  { path: '**', redirectTo: 'products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
