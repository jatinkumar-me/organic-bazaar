import { Component } from '@angular/core';

@Component({
  selector: 'app-layout',
  template: `
    <app-navbar></app-navbar>
      <div class="container">
          <router-outlet></router-outlet>
      </div>
  `,
  styles: `
  .container {
    max-width: 60rem;
  }

`
})
export class LayoutComponent {

}
