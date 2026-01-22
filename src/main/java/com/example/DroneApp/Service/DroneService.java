package com.example.DroneApp.Service;

import com.example.DroneApp.Dto.DroneHeartbeatRequestDto;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Dto.OrderSummaryDto;
import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Drone;
import com.example.DroneApp.Model.Location;
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
@Transactional
public class DroneService {

    private final DroneRepository droneRepository;
    private final OrderRepository orderRepository;

    public DroneService(DroneRepository droneRepository, OrderRepository orderRepository) {
        this.droneRepository = droneRepository;
        this.orderRepository = orderRepository;
    }

    // Reserve first available job
    public OrderResponseDto reserveJob(User droneUser) {
        Drone drone = getDroneByUser(droneUser);

        if (drone.isBroken())
            throw new IllegalStateException("Drone is broken");

        if (drone.getCurrentOrder() != null)
            throw new IllegalStateException("Drone already has an order");

        Order order = orderRepository.findByStatus(OrderStatus.PENDING)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No available orders"));

        assignOrderToDrone(drone, order);
        return OrderResponseDto.from(order);
    }

    // Grab order (origin or broken drone)
    public OrderResponseDto grabOrder(Long orderId, User droneUser) {
        Drone drone = getDroneByUser(droneUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (drone.isBroken())
            throw new IllegalStateException("Drone is broken");

        if (drone.getCurrentOrder() != null)
            throw new IllegalStateException("Drone already has an order");

            if (order.getStatus() != OrderStatus.PENDING)
            throw new IllegalStateException("Order is not available");

        assignOrderToDrone(drone, order);
        return OrderResponseDto.from(order);
    }

     // Heartbeat (location update)
    public DroneResponseDto heartbeat(DroneHeartbeatRequestDto dto, User droneUser) {
        Drone drone = getDroneByUser(droneUser);

        drone.setCurrentLocation(new Location(dto.getLatitude(), dto.getLongitude()));
        drone.setLastHeartbeat(LocalDateTime.now());

        return DroneResponseDto.from(droneRepository.save(drone));
    }

    // Mark self broken
    public DroneResponseDto markSelfBroken(User droneUser) {
        Drone drone = getDroneByUser(droneUser);
        drone.setBroken(true);

        if (drone.getCurrentOrder() != null) {
            Order order = drone.getCurrentOrder();
            order.setAssignedDrone(null);
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            drone.setCurrentOrder(null);
        }

        return DroneResponseDto.from(droneRepository.save(drone));
    }

    // Mark order delivered
    public OrderResponseDto markOrderDelivered(Long orderId, User droneUser) {
        Drone drone = getDroneByUser(droneUser);
        Order order = validateCurrentOrder(drone, orderId);

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        drone.setCurrentOrder(null);
        droneRepository.save(drone);

        return OrderResponseDto.from(order);
    }

    // Mark order failed
    public OrderResponseDto markOrderFailed(Long orderId, User droneUser) {
        Drone drone = getDroneByUser(droneUser);
        Order order = validateCurrentOrder(drone, orderId);

        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        drone.setCurrentOrder(null);
        droneRepository.save(drone);

        return OrderResponseDto.from(order);
    }

    // Get current assigned order
    public OrderResponseDto getCurrentOrder(User droneUser) {
        Drone drone = getDroneByUser(droneUser);

        if (drone.getCurrentOrder() == null)
            throw new EntityNotFoundException("No active order");

        return OrderResponseDto.from(drone.getCurrentOrder());
    }

    public Drone getDroneByUser(User user) {
        return droneRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));
    }
    public User getDroneByUserId(Long userId) {
        User user=new User();
        user.setId(userId);
        return droneRepository.findByUser(user).get().getUser();
    }

    private void assignOrderToDrone(Drone drone, Order order) {
        order.setAssignedDrone(drone);
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);

        drone.setCurrentOrder(order);
        droneRepository.save(drone);
    }

    private Order validateCurrentOrder(Drone drone, Long orderId) {
        Order order = drone.getCurrentOrder();

        if (order == null || !order.getId().equals(orderId))
            throw new EntityNotFoundException("Order not assigned to this drone");

        return order;
    }

    @Transactional
    public DroneResponseDto markBroken(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        drone.setBroken(true);

        // If drone has a current order, unassign it
        Order currentOrder = drone.getCurrentOrder();
        if (currentOrder != null) {
            currentOrder.setStatus(OrderStatus.PENDING);
            currentOrder.setAssignedDrone(null);
            orderRepository.save(currentOrder);
            drone.setCurrentOrder(null);
        }

        return mapToDto(droneRepository.save(drone));
    }

    @Transactional
    public DroneResponseDto markFixed(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        drone.setBroken(false);
        return mapToDto(droneRepository.save(drone));
    }

    // Get all drones
    public List<DroneResponseDto> getAllDrones() {
        return droneRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Mapping Drone -> DroneResponseDto
    private DroneResponseDto mapToDto(Drone drone) {
        DroneResponseDto dto = new DroneResponseDto();
        dto.setDroneId(drone.getId());
        dto.setName(drone.getName());
        dto.setBroken(drone.isBroken());
        dto.setCurrentLocation(drone.getCurrentLocation());

        if (drone.getCurrentOrder() != null) {
            OrderSummaryDto orderDto = new OrderSummaryDto();
            orderDto.setOrderId(drone.getCurrentOrder().getId());
            orderDto.setOrigin(drone.getCurrentOrder().getOrigin());
            orderDto.setDestination(drone.getCurrentOrder().getDestination());
            orderDto.setStatus(drone.getCurrentOrder().getStatus());
            dto.setCurrentOrder(orderDto);
        }

        return dto;
    }

}
