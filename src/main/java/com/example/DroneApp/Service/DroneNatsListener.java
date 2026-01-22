package com.example.DroneApp.Service;

import com.example.DroneApp.Dto.DroneHeartbeatRequestDto;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class DroneNatsListener {

    private final Connection natsConnection;
    private final DroneService droneService;
    private final ObjectMapper objectMapper;

    public DroneNatsListener(Connection natsConnection, DroneService droneService) {
        this.natsConnection = natsConnection;
        this.droneService = droneService;
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void subscribeToHeartbeats() {
        Dispatcher dispatcher = natsConnection.createDispatcher(msg -> { /* empty */ });

        dispatcher.subscribe("drones.heartbeat", msg -> {
            try {
                handleHeartbeat(msg);
            } catch (Exception e) {
                // Log the error instead of throwing
                System.err.println("Error processing heartbeat: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void handleHeartbeat(Message msg) throws Exception {
        // Deserialize message
        HeartbeatPayload payload = objectMapper.readValue(msg.getData(), HeartbeatPayload.class);

        // Fetch User by ID (you can implement a service to fetch User)
        User droneUser = droneService.getDroneByUserId(payload.getUserId());

        DroneHeartbeatRequestDto dto = payload.getHeartbeat();

        // Update drone location
        DroneResponseDto response = droneService.heartbeat(dto, droneUser);

        System.out.println("Heartbeat processed for drone: " + response.getDroneId());
    }

    // Inner class for deserialization
    public static class HeartbeatPayload {
        private Long userId;
        private DroneHeartbeatRequestDto heartbeat;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public DroneHeartbeatRequestDto getHeartbeat() { return heartbeat; }
        public void setHeartbeat(DroneHeartbeatRequestDto heartbeat) { this.heartbeat = heartbeat; }
    }
}
