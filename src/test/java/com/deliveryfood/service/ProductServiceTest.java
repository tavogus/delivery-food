package com.deliveryfood.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deliveryfood.dto.ProductRequestDTO;
import com.deliveryfood.dto.ProductResponseDTO;
import com.deliveryfood.entity.Product;
import com.deliveryfood.entity.Restaurant;
import com.deliveryfood.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Restaurant restaurant;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        product = new Product();
        product.setId(1L);
        product.setRestaurant(restaurant);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("10.99"));
        product.setCategory("PIZZA");
        product.setImageUrl("https://example.com/image.jpg");
        product.setAvailable(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        requestDTO = new ProductRequestDTO(
            1L,
            "Test Product",
            "Test Description",
            new BigDecimal("10.99"),
            "PIZZA",
            "https://example.com/image.jpg",
            true
        );
    }

    @Test
    void createProduct_ShouldReturnProductResponseDTO() {
        when(restaurantService.findById(1L)).thenReturn(restaurant);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.createProduct(requestDTO);

        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getRestaurant().getId(), response.restaurantId());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getCategory(), response.category());
        assertEquals(product.getImageUrl(), response.imageUrl());
        assertEquals(product.isAvailable(), response.available());

        verify(restaurantService).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProduct_ShouldReturnProductResponseDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.getProduct(1L);

        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());

        verify(productRepository).findById(1L);
    }

    @Test
    void getProduct_WhenNotFound_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            productService.getProduct(1L)
        );

        verify(productRepository).findById(1L);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProductResponseDTO() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDTO> responses = productService.getAllProducts();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(product.getId(), responses.get(0).id());
        assertEquals(product.getName(), responses.get(0).name());

        verify(productRepository).findAll();
    }

    @Test
    void getProductsByRestaurant_ShouldReturnListOfProductResponseDTO() {
        when(productRepository.findByRestaurantId(1L)).thenReturn(List.of(product));

        List<ProductResponseDTO> responses = productService.getProductsByRestaurant(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(product.getId(), responses.get(0).id());
        assertEquals(product.getName(), responses.get(0).name());
        assertEquals(restaurant.getId(), responses.get(0).restaurantId());

        verify(productRepository).findByRestaurantId(1L);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProductResponseDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(restaurantService.findById(1L)).thenReturn(restaurant);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.updateProduct(1L, requestDTO);

        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(requestDTO.name(), response.name());
        assertEquals(requestDTO.description(), response.description());

        verify(productRepository).findById(1L);
        verify(restaurantService).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenNotFound_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            productService.updateProduct(1L, requestDTO)
        );

        verify(productRepository).findById(1L);
        verify(restaurantService, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenExists_ShouldReturnTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenNotExists_ShouldReturnFalse() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean result = productService.deleteProduct(1L);

        assertFalse(result);
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }
} 