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
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deliveryfood.dto.RestaurantRequestDTO;
import com.deliveryfood.dto.RestaurantResponseDTO;
import com.deliveryfood.entity.Restaurant;
import com.deliveryfood.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Test Description");
        restaurant.setAddress("Test Address");
        restaurant.setPhone("(11) 99999-9999");
        restaurant.setOpeningHours("09:00-22:00");
        restaurant.setDeliveryFee(new BigDecimal("5.99"));
        restaurant.setMinimumOrder(new BigDecimal("20.00"));
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(LocalDateTime.now());

        requestDTO = new RestaurantRequestDTO(
            "Test Restaurant",
            "Test Description",
            "Test Address",
            "(11) 99999-9999",
            "09:00-22:00",
            new BigDecimal("5.99"),
            new BigDecimal("20.00")
        );
    }

    @Test
    void createRestaurant_ShouldReturnRestaurantResponseDTO() {
        when(restaurantRepository.existsByName(anyString())).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponseDTO response = restaurantService.createRestaurant(requestDTO);

        assertNotNull(response);
        assertEquals(restaurant.getId(), response.id());
        assertEquals(restaurant.getName(), response.name());
        assertEquals(restaurant.getDescription(), response.description());
        assertEquals(restaurant.getAddress(), response.address());
        assertEquals(restaurant.getPhone(), response.phone());
        assertEquals(restaurant.getOpeningHours(), response.openingHours());
        assertEquals(restaurant.getDeliveryFee(), response.deliveryFee());
        assertEquals(restaurant.getMinimumOrder(), response.minimumOrder());

        verify(restaurantRepository).existsByName(requestDTO.name());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void createRestaurant_WhenNameExists_ShouldThrowException() {
        when(restaurantRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            restaurantService.createRestaurant(requestDTO)
        );

        verify(restaurantRepository).existsByName(requestDTO.name());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void getRestaurant_ShouldReturnRestaurantResponseDTO() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO response = restaurantService.getRestaurant(1L);

        assertNotNull(response);
        assertEquals(restaurant.getId(), response.id());
        assertEquals(restaurant.getName(), response.name());
        assertEquals(restaurant.getDescription(), response.description());

        verify(restaurantRepository).findById(1L);
    }

    @Test
    void getRestaurant_WhenNotFound_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            restaurantService.getRestaurant(1L)
        );

        verify(restaurantRepository).findById(1L);
    }

    @Test
    void getAllRestaurants_ShouldReturnListOfRestaurantResponseDTO() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantResponseDTO> responses = restaurantService.getAllRestaurants();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(restaurant.getId(), responses.get(0).id());
        assertEquals(restaurant.getName(), responses.get(0).name());

        verify(restaurantRepository).findAll();
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurantResponseDTO() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponseDTO response = restaurantService.updateRestaurant(1L, requestDTO);

        assertNotNull(response);
        assertEquals(restaurant.getId(), response.id());
        assertEquals(requestDTO.name(), response.name());
        assertEquals(requestDTO.description(), response.description());

        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void updateRestaurant_WhenNotFound_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            restaurantService.updateRestaurant(1L, requestDTO)
        );

        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void deleteRestaurant_WhenExists_ShouldReturnTrue() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        doNothing().when(restaurantRepository).deleteById(1L);

        boolean result = restaurantService.deleteRestaurant(1L);

        assertTrue(result);
        verify(restaurantRepository).existsById(1L);
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void deleteRestaurant_WhenNotExists_ShouldReturnFalse() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        boolean result = restaurantService.deleteRestaurant(1L);

        assertFalse(result);
        verify(restaurantRepository).existsById(1L);
        verify(restaurantRepository, never()).deleteById(1L);
    }

    @Test
    void findById_ShouldReturnRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.findById(1L);

        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());

        verify(restaurantRepository).findById(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            restaurantService.findById(1L)
        );

        verify(restaurantRepository).findById(1L);
    }
} 