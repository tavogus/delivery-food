package com.deliveryfood.service;

import com.deliveryfood.dto.RestaurantRequestDTO;
import com.deliveryfood.dto.RestaurantResponseDTO;
import com.deliveryfood.entity.Restaurant;
import com.deliveryfood.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setAddress(request.address());
        restaurant.setPhone(request.phone());
        restaurant.setOpeningHours(request.openingHours());
        restaurant.setDeliveryFee(request.deliveryFee());
        restaurant.setMinimumOrder(request.minimumOrder());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponseDTO(savedRestaurant);
    }

    public RestaurantResponseDTO getRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .map(this::mapToResponseDTO).orElseThrow(() -> new RuntimeException("Restaurant with id " + id + " not found"));
    }

    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO request) {
        Restaurant restaurant = findById(id);

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setAddress(request.address());
        restaurant.setPhone(request.phone());
        restaurant.setOpeningHours(request.openingHours());
        restaurant.setDeliveryFee(request.deliveryFee());
        restaurant.setMinimumOrder(request.minimumOrder());

        restaurant = restaurantRepository.save(restaurant);

        return mapToResponseDTO(restaurant);

    }

    public boolean deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            return false;
        }
        restaurantRepository.deleteById(id);
        return true;
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant with id " + id + " not found"));
    }

    private RestaurantResponseDTO mapToResponseDTO(Restaurant restaurant) {
        return new RestaurantResponseDTO(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getAddress(),
            restaurant.getPhone(),
            restaurant.getOpeningHours(),
            restaurant.getDeliveryFee(),
            restaurant.getMinimumOrder(),
            restaurant.getCreatedAt(),
            restaurant.getUpdatedAt()
        );
    }
} 