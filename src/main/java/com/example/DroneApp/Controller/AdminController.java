package com.example.DroneApp.Controller;

import com.example.DroneApp.Dto.OrderRequestDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Dto.OrderUpdateLocationDto;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Service.OrderService;
import com.example.DroneApp.Service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderService orderService;
    private final DroneService droneService;

    public AdminController(OrderService orderService, DroneService droneService) {
        this.orderService = orderService;
        this.droneService = droneService;
    }

    //Get multiple orders in bulk (all orders)
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    //Change origin or destination for an order
    @PutMapping("/orders/{orderId}/location")
    public ResponseEntity<OrderResponseDto> updateOrderLocation(
            @PathVariable Long orderId,
            @RequestBody OrderRequestDto dto
    ) {
        return ResponseEntity.ok(orderService.updateOrderLocation(orderId, dto));
    }

    //Get a list of all drones
    @GetMapping("/drones")
    public ResponseEntity<List<DroneResponseDto>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }

    //Mark drone as broken
    @PutMapping("/drones/{droneId}/broken")
    public ResponseEntity<DroneResponseDto> markDroneBroken(@PathVariable Long droneId) {
        return ResponseEntity.ok(droneService.markBroken(droneId));
    }

    //Mark drone as fixed
    @PutMapping("/drones/{droneId}/fixed")
    public ResponseEntity<DroneResponseDto> markDroneFixed(@PathVariable Long droneId) {
        return ResponseEntity.ok(droneService.markFixed(droneId));
    }
}
