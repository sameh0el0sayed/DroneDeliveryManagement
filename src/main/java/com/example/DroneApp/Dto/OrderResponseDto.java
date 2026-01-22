package com.example.DroneApp.Dto;

import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Location;
import com.example.DroneApp.Model.Order;

public class OrderResponseDto {

    private Long orderId;
    private Long customerId;
    private Location origin;
    private Location destination;
    private OrderStatus status;
    private Long assignedDroneId;

    public static OrderResponseDto from(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setOrigin(order.getOrigin());
        dto.setDestination(order.getDestination());
        dto.setStatus(order.getStatus());
        dto.setAssignedDroneId(
                order.getAssignedDrone() != null
                        ? order.getAssignedDrone().getId()
                        : null
        );
        return dto;
    }

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
