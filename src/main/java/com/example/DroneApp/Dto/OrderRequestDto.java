package com.example.DroneApp.Dto;

import com.example.DroneApp.Model.Location;

public class OrderRequestDto {
    private Location origin;
    private Location destination;

    public Location getOrigin() { return origin; }
    public void setOrigin(Location origin) { this.origin = origin; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }
}
