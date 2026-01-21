package com.example.DroneApp.Service;

import com.example.DroneApp.Dto.OrderRequestDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Drone;
import com.example.DroneApp.Model.Order;
import com.example.DroneApp.Model.User;
import com.example.DroneApp.Repository.DroneRepository;
import com.example.DroneApp.Repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DroneRepository droneRepository;

    public OrderService(OrderRepository orderRepository, DroneRepository droneRepository) {
        this.orderRepository = orderRepository;
        this.droneRepository = droneRepository;
    }

    // Create a new order
    @Transactional
    public OrderResponseDto createOrder(User customer, OrderRequestDto request) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrigin(request.getOrigin());
        order.setDestination(request.getDestination());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return mapToDto(order);
    }

    // Withdraw an order (if not picked up)
    @Transactional
    public OrderResponseDto withdrawOrder(Long orderId, User customer) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot withdraw order that is already in progress");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return mapToDto(order);
    }

    // Get all orders for a customer
    public List<OrderResponseDto> getOrdersByCustomer(User customer) {
        return orderRepository.findByCustomer(customer)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Admin: update origin/destination
    @Transactional
    public OrderResponseDto updateOrderLocation(Long orderId, OrderRequestDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (request.getOrigin() != null) order.setOrigin(request.getOrigin());
        if (request.getDestination() != null) order.setDestination(request.getDestination());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        return mapToDto(order);
    }

    // Get orders by status
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get orders assigned to a specific drone
    public List<OrderResponseDto> getOrdersByDrone(Drone drone) {
        return orderRepository.findByAssignedDrone(drone)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto>  getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    // Mapping Order -> OrderResponseDto
    private OrderResponseDto mapToDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setOrigin(order.getOrigin());
        dto.setDestination(order.getDestination());
        dto.setStatus(order.getStatus());
        dto.setAssignedDroneId(order.getAssignedDrone() != null ? order.getAssignedDrone().getId() : null);
        return dto;
    }


}
