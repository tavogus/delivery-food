import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RestaurantService, Restaurant } from '../restaurant.service';
import { ProductService, Product } from '../product.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-restaurant-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './restaurant-detail.component.html',
  styleUrl: './restaurant-detail.component.css'
})
export class RestaurantDetailComponent implements OnInit {
  restaurant: Restaurant | undefined;
  products: Product[] = [];

  constructor(
    private route: ActivatedRoute,
    private restaurantService: RestaurantService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      const restaurantId = parseInt(id, 10);
      if (!isNaN(restaurantId)) {
        this.restaurantService.getRestaurantById(restaurantId).subscribe(
          (restaurant) => this.restaurant = restaurant
        );

        this.productService.getProductsByRestaurant(restaurantId).subscribe(
          (products) => this.products = products
        );
      } else {
        console.error('Invalid restaurant ID:', id);
      }
    } else {
      console.error('No restaurant ID provided');
    }
  }
}
