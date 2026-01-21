package com.example.DroneApp.Dto;

public class DroneRequestDto {
    private Double latitude;
    private Double longitude;
    private Long orderId; // for reserving an order

    // Getters and setters
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}

