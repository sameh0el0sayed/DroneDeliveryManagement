package com.example.DroneApp.Service;

import com.example.DroneApp.Dto.DroneRequestDto;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Dto.OrderSummaryDto;
import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Drone;
import com.example.DroneApp.Model.Location;
import com.example.DroneApp.Model.Order;
import com.example.DroneApp.Repository.DroneRepository;
import com.example.DroneApp.Repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final OrderRepository orderRepository;

    public DroneService(DroneRepository droneRepository, OrderRepository orderRepository) {
        this.droneRepository = droneRepository;
        this.orderRepository = orderRepository;
    }

    // Get all drones
    public List<DroneResponseDto> getAllDrones() {
        return droneRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update drone location and heartbeat
    @Transactional
    public DroneResponseDto updateLocation(Long droneId, DroneRequestDto request) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

        drone.setCurrentLocation(new Location(request.getLatitude(), request.getLongitude()));
        drone.setLastHeartbeat(LocalDateTime.now());
        droneRepository.save(drone);

        return mapToDto(drone);
    }

    // Mark drone as broken
    @Transactional
    public DroneResponseDto markBroken(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

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

    // Mark drone as fixed
    @Transactional
    public DroneResponseDto markFixed(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

        drone.setBroken(false);
        return mapToDto(droneRepository.save(drone));
    }

    // Assign an order to a drone
    @Transactional
    public DroneResponseDto reserveOrder(Long droneId, Long orderId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

        if (drone.isBroken())
            throw new RuntimeException("Cannot assign order to a broken drone");

        if (drone.getCurrentOrder() != null)
            throw new RuntimeException("Drone already has an assigned order");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING)
            throw new EntityNotFoundException("Order is not available for pickup");

        order.setAssignedDrone(drone);
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);

        drone.setCurrentOrder(order);
        return mapToDto(droneRepository.save(drone));
    }

    // Mark order delivered
    @Transactional
    public DroneResponseDto markOrderDelivered(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

        Order order = drone.getCurrentOrder();
        if (order == null) throw new EntityNotFoundException("No current order assigned");

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        drone.setCurrentOrder(null);
        return mapToDto(droneRepository.save(drone));
    }

    // Mark order failed
    @Transactional
    public DroneResponseDto markOrderFailed(Long droneId) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone not found"));

        Order order = drone.getCurrentOrder();
        if (order == null) throw new EntityNotFoundException("No current order assigned");

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        drone.setCurrentOrder(null);
        return mapToDto(droneRepository.save(drone));
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
