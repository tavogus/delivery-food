# Delivery Food Application

A food delivery application similar to iFood, built with Spring Boot 3.3.10 and Java 21.

## Prerequisites

- Java 21
- Maven
- Docker and Docker Compose (for running PostgreSQL)

## Database Setup

### Option 1: Using Docker Compose (Recommended)

1. Start the PostgreSQL database using Docker Compose:
```bash
docker-compose up -d
```

This will start PostgreSQL with the following configuration:
- Database URL: `jdbc:postgresql://localhost:5432/fooddb`
- Username: `fooduser`
- Password: `foodpass`

### Option 2: Manual Setup

1. Create a PostgreSQL database named `fooddb`:
```sql
CREATE DATABASE fooddb;
```

2. Create a user with the required permissions:
```sql
CREATE USER fooduser WITH PASSWORD 'foodpass';
GRANT ALL PRIVILEGES ON DATABASE fooddb TO fooduser;
```

## Configuration

The application is configured to use PostgreSQL with the following settings:
- Database URL: `jdbc:postgresql://localhost:5432/fooddb`
- Username: `fooduser`
- Password: `foodpass`

You can modify these settings in `src/main/resources/application.yml` if needed.

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

- `src/main/java/com/deliveryfood/`: Main package root.
  - `controller/`: Contains REST API controllers.
  - `dto/`: Contains Data Transfer Objects (DTOs) for request/response mapping.
  - `entity/`: Contains JPA entity classes representing database tables.
  - `repository/`: Contains Spring Data JPA repositories for database access.
  - `service/`: Contains business logic service classes.
  - `DeliveryFoodApplication.java`: The main Spring Boot application class.
- `src/main/resources/`: Contains application resources.
  - `application.yml`: Application configuration file (database, server port, etc.).
  - `db/`: Contains database migration scripts (e.g., Flyway or Liquibase).

## Main Entities

- User: Represents application users
- Restaurant: Represents food delivery restaurants
- Product: Represents food items available in restaurants
- Order: Represents user orders
- OrderItem: Represents items within an order

## API Endpoints

### Users (`/api/users`)

- **POST /****: Create a new user.
  - Request Body: `UserRequestDTO`
  - Response: `UserResponseDTO`
- **GET /{id}**: Get user details by ID.
  - Response: `UserResponseDTO`
- **GET /****: Get a list of all users.
  - Response: `List<UserResponseDTO>`
- **PUT /{id}**: Update user details by ID.
  - Request Body: `UserRequestDTO`
  - Response: `UserResponseDTO`
- **DELETE /{id}**: Delete a user by ID.
  - Response: `204 No Content` on success, `404 Not Found` otherwise.

### Restaurants (`/api/restaurants`)

- **POST /****: Create a new restaurant.
  - Request Body: `RestaurantRequestDTO`
  - Response: `RestaurantResponseDTO`
- **GET /{id}**: Get restaurant details by ID.
  - Response: `RestaurantResponseDTO`
- **GET /****: Get a list of all restaurants.
  - Response: `List<RestaurantResponseDTO>`
- **PUT /{id}**: Update restaurant details by ID.
  - Request Body: `RestaurantRequestDTO`
  - Response: `RestaurantResponseDTO`
- **DELETE /{id}**: Delete a restaurant by ID.
  - Response: `204 No Content` on success, `404 Not Found` otherwise.

### Products (`/api/products`)

- **POST /****: Create a new product.
  - Request Body: `ProductRequestDTO`
  - Response: `ProductResponseDTO`
- **GET /{id}**: Get product details by ID.
  - Response: `ProductResponseDTO`
- **GET /****: Get a list of all products.
  - Response: `List<ProductResponseDTO>`
- **GET /restaurant/{restaurantId}**: Get a list of products for a specific restaurant.
  - Response: `List<ProductResponseDTO>`
- **PUT /{id}**: Update product details by ID.
  - Request Body: `ProductRequestDTO`
  - Response: `ProductResponseDTO`
- **DELETE /{id}**: Delete a product by ID.
  - Response: `204 No Content` on success, `404 Not Found` otherwise.

### Orders (`/api/orders`)

- **POST /****: Create a new order.
  - Request Body: `OrderRequestDTO`
  - Response: `OrderResponseDTO`
- **GET /{id}**: Get order details by ID.
  - Response: `OrderResponseDTO`
- **GET /****: Get a list of all orders.
  - Response: `List<OrderResponseDTO>`
- **GET /user/{userId}**: Get a list of orders for a specific user.
  - Response: `List<OrderResponseDTO>`
- **GET /restaurant/{restaurantId}**: Get a list of orders for a specific restaurant.
  - Response: `List<OrderResponseDTO>`
- **PUT /{id}/status**: Update the status of an order.
  - Request Body: `OrderStatus` (e.g., `"CONFIRMED"`, `"DELIVERED"`)
  - Response: `OrderResponseDTO`
- **POST /pay/{orderId}/user/{userId}**: Mark an order as paid.
  - Response: `OrderResponseDTO` 