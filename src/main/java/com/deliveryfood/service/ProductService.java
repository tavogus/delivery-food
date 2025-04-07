package com.deliveryfood.service;

import com.deliveryfood.dto.ProductRequestDTO;
import com.deliveryfood.dto.ProductResponseDTO;
import com.deliveryfood.entity.Product;
import com.deliveryfood.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantService restaurantService;

    public ProductService(ProductRepository productRepository, RestaurantService restaurantService) {
        this.productRepository = productRepository;
        this.restaurantService = restaurantService;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = new Product();
        product.setRestaurant(restaurantService.findById(request.restaurantId()));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
        product.setAvailable(request.available());

        product = productRepository.save(product);
        return mapToResponseDTO(product);

    }

    public ProductResponseDTO getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByRestaurant(Long restaurantId) {
        return productRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));

        product.setRestaurant(restaurantService.findById(request.restaurantId()));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
        product.setAvailable(request.available());

        product = productRepository.save(product);

        return mapToResponseDTO(product);
    }

    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getRestaurant().getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategory(),
            product.getImageUrl(),
            product.isAvailable(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
} 