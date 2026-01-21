package com.example.DroneApp.Dto;

import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Location;

public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private Location origin;
    private Location destination;
    private OrderStatus status;
    private Long assignedDroneId;

    // Getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Location getOrigin() { return origin; }
    public void setOrigin(Location origin) { this.origin = origin; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Long getAssignedDroneId() { return assignedDroneId; }
    public void setAssignedDroneId(Long assignedDroneId) { this.assignedDroneId = assignedDroneId; }
}
