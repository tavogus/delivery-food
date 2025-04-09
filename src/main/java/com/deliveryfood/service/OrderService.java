package com.deliveryfood.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.deliveryfood.dto.OrderItemRequestDTO;
import com.deliveryfood.dto.OrderItemResponseDTO;
import com.deliveryfood.dto.OrderRequestDTO;
import com.deliveryfood.dto.OrderResponseDTO;
import com.deliveryfood.entity.Order;
import com.deliveryfood.entity.OrderItem;
import com.deliveryfood.entity.OrderStatus;
import com.deliveryfood.entity.Product;
import com.deliveryfood.entity.Restaurant;
import com.deliveryfood.entity.User;
import com.deliveryfood.repository.OrderRepository;
import com.deliveryfood.repository.ProductRepository;
import com.deliveryfood.repository.RestaurantRepository;
import com.deliveryfood.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }

    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryAddress(request.deliveryAddress());
        order.setPaymentMethod(request.paymentMethod());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequestDTO itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            orderItem.setNotes(itemRequest.notes());
            
            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        return mapToResponseDTO(order);
    }

    public OrderResponseDTO getOrder(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponseDTO).orElseThrow(() -> new RuntimeException("Order with id " + id + " not found"));
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with id " + id + " not found"));

        if (order.isPaid()) {
            order.setStatus(status);
            order = orderRepository.save(order);
        }

        return mapToResponseDTO(order);
    }
    
    public OrderResponseDTO payOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with id " + orderId + " not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Order does not belong to the specified user");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not in PENDING status");
        }

        order.setPaid(true);

        order = orderRepository.save(order);
        return mapToResponseDTO(order);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getSubtotal(),
                    item.getNotes(),
                    item.getCreatedAt()))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
            order.getId(),
            order.getOrderNumber(),
            order.getUser().getId(),
            order.getUser().getName(),
            order.getRestaurant().getId(),
            order.getRestaurant().getName(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getDeliveryAddress(),
            order.getPaymentMethod(),
            order.isPaid(),
            items,
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }

    private String generateOrderNumber() {
        String orderNumber;
        do {
            orderNumber = UUID.randomUUID().toString().substring(0, 8);
        } while (orderRepository.existsByOrderNumber(orderNumber));
        return orderNumber;
    }
} 