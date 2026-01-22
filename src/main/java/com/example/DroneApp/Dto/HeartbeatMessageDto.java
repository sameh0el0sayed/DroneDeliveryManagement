package com.example.DroneApp.Dto;

public class HeartbeatMessageDto {
    private Long userId;
    private DroneHeartbeatRequestDto heartbeat;

    public HeartbeatMessageDto(Long userId, DroneHeartbeatRequestDto heartbeat) {
        this.userId = userId;
        this.heartbeat = heartbeat;
    }

    public Long getUserId() { return userId; }
    public DroneHeartbeatRequestDto getHeartbeat() { return heartbeat; }
}
