package com.example.DroneApp.Controller;

import com.example.DroneApp.Dto.DroneHeartbeatRequestDto;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Dto.HeartbeatMessageDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Security.MyUserDetails;
import com.example.DroneApp.Service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

@RestController
@RequestMapping("/api/drone")
public class DroneController {

    private final DroneService droneService;
    private final Connection natsConnection;
    private final ObjectMapper objectMapper;

    public DroneController(DroneService droneService, Connection natsConnection, ObjectMapper objectMapper) {
        this.droneService = droneService;
        this.natsConnection = natsConnection;
        this.objectMapper = objectMapper;
    }

    // Reserve a job
    @PostMapping("/orders/reserve")
    public ResponseEntity<OrderResponseDto> reserveJob(
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.reserveJob(userDetails.getUser())
        );
    }

    // Grab an order (origin or broken drone)
    @PostMapping("/orders/{orderId}/grab")
    public ResponseEntity<OrderResponseDto> grabOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.grabOrder(orderId, userDetails.getUser())
        );
    }

    // Mark order delivered
    @PutMapping("/orders/{orderId}/delivered")
    public ResponseEntity<OrderResponseDto> markDelivered(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.markOrderDelivered(orderId, userDetails.getUser())
        );
    }

    // Mark order failed
    @PutMapping("/orders/{orderId}/failed")
    public ResponseEntity<OrderResponseDto> markFailed(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.markOrderFailed(orderId, userDetails.getUser())
        );
    }

    // Mark drone as broken
    @PutMapping("/broken")
    public ResponseEntity<DroneResponseDto> markBroken(
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.markSelfBroken(userDetails.getUser())
        );
    }

     // Heartbeat: update location + get status
     @PutMapping("/heartbeat")
     public ResponseEntity<String> heartbeat(
             @RequestBody DroneHeartbeatRequestDto dto,
             @AuthenticationPrincipal MyUserDetails userDetails
     ) throws Exception {
         // Construct a message
         var payload = objectMapper.writeValueAsBytes(new HeartbeatMessageDto(userDetails.getUser().getId(), dto));

         // Publish to NATS
         natsConnection.publish("drones.heartbeat", payload);

         return ResponseEntity.ok("Heartbeat sent to NATS");
     }

     // Get current assigned order
     @GetMapping("/orders/current")
    public ResponseEntity<OrderResponseDto> getCurrentOrder(
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                droneService.getCurrentOrder(userDetails.getUser())
        );
    }
}
