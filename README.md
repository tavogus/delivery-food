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

- `src/main/java/com/deliveryfood/entity/`: Contains all JPA entities
- `src/main/resources/db/`: Contains database migration scripts
- `src/main/resources/application.yml`: Application configuration

## Main Entities

- User: Represents application users
- Restaurant: Represents food delivery restaurants
- Product: Represents food items available in restaurants
- Order: Represents user orders
- OrderItem: Represents items within an order

## API Endpoints

The API endpoints will be documented here as they are implemented. 