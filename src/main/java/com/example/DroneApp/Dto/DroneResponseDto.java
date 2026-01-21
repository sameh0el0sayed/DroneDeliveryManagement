package com.example.DroneApp.Dto;

import com.example.DroneApp.Model.Location;
import com.example.DroneApp.Enum.OrderStatus;

public class DroneResponseDto {
    private Long droneId;
    private String name;
    private boolean isBroken;
    private Location currentLocation;
    private OrderSummaryDto currentOrder;

    // Getters and setters
    public Long getDroneId() { return droneId; }
    public void setDroneId(Long droneId) { this.droneId = droneId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isBroken() { return isBroken; }
    public void setBroken(boolean broken) { isBroken = broken; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public OrderSummaryDto getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(OrderSummaryDto currentOrder) { this.currentOrder = currentOrder; }
}
