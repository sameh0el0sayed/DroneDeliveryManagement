package com.example.DroneApp.Dto;

import com.example.DroneApp.Model.Location;
import com.example.DroneApp.Enum.OrderStatus;

public class OrderSummaryDto {
    private Long orderId;
    private Location origin;
    private Location destination;
    private OrderStatus status;

    // Getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Location getOrigin() { return origin; }
    public void setOrigin(Location origin) { this.origin = origin; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
