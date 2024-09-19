import { Component, OnInit } from '@angular/core';
import { Product, ProductRequestParams, ProductsService, SortOrder } from '../products.service';
import { debounceTime, Subject } from 'rxjs';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  sort = 'name';
  order = SortOrder.INC;
  category = '';
  search = '';

  private search$ = new Subject<string>();

  constructor(private productService: ProductsService) { }

  ngOnInit(): void {
    this.search$.pipe(
      debounceTime(300),
    ).subscribe(() => this.loadProducts())
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts(this.getProductRequestParams()).subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error("Error fetching products", err),
    });
  }

  getProductRequestParams(): ProductRequestParams {
    const productRequestParams: ProductRequestParams = {
      sort: this.sort,
      order: this.order,
      category: this.category,
      search: this.search,
    }
    return productRequestParams;
  }

  onSearchChange(event: string): void {
    this.search$.next(event);
  }

  onCategoryChange(category: string): void {
    this.category = category;
    this.loadProducts();
  }

  onSortChange(sortBy: string, order: SortOrder): void {
    this.sort = sortBy;
    this.order = order;
    this.loadProducts();
  }
}
