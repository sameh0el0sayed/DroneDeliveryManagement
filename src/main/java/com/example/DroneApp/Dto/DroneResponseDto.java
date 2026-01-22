package com.example.DroneApp.Dto;

import com.example.DroneApp.Model.Drone;
import com.example.DroneApp.Model.Order;
import com.example.DroneApp.Model.Location;

public class DroneResponseDto {

    private Long droneId;
    private String name;
    private boolean broken;
    private Location currentLocation;
    private OrderSummaryDto currentOrder;

    public static DroneResponseDto from(Drone drone) {
        DroneResponseDto dto = new DroneResponseDto();
        dto.setDroneId(drone.getId());
        dto.setName(drone.getName());
        dto.setBroken(drone.isBroken());
        dto.setCurrentLocation(drone.getCurrentLocation());

        Order order = drone.getCurrentOrder();
        if (order != null) {
            dto.setCurrentOrder(OrderSummaryDto.from(order));
        }

        return dto;
    }

    public Long getDroneId() { return droneId; }
    public void setDroneId(Long droneId) { this.droneId = droneId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isBroken() { return broken; }
    public void setBroken(boolean broken) { this.broken = broken; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public OrderSummaryDto getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(OrderSummaryDto currentOrder) { this.currentOrder = currentOrder; }
}
