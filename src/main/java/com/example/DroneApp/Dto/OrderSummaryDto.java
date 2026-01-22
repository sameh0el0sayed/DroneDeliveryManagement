package com.example.DroneApp.Dto;

import com.example.DroneApp.Model.Location;
import com.example.DroneApp.Model.Order;
import com.example.DroneApp.Enum.OrderStatus;

public class OrderSummaryDto {

    private Long orderId;
    private Location origin;
    private Location destination;
    private OrderStatus status;

    public static OrderSummaryDto from(Order order) {
        OrderSummaryDto dto = new OrderSummaryDto();
        dto.setOrderId(order.getId());
        dto.setOrigin(order.getOrigin());
        dto.setDestination(order.getDestination());
        dto.setStatus(order.getStatus());
        return dto;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Location getOrigin() { return origin; }
    public void setOrigin(Location origin) { this.origin = origin; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
