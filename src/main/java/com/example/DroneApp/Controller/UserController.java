package com.example.DroneApp.Controller;

import com.example.DroneApp.Dto.OrderRequestDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Model.User;
import com.example.DroneApp.Security.MyUserDetails;
import com.example.DroneApp.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
public class UserController {

    private final OrderService orderService;

    public UserController(OrderService orderService) {
        this.orderService = orderService;
    }

    //Submit a new order
    @PostMapping
    public ResponseEntity<OrderResponseDto> submitOrder(
            @AuthenticationPrincipal MyUserDetails customUser,
            @RequestBody OrderRequestDto requestDto
    ) {
        User user = customUser.getUser(); // This is your actual entity
        OrderResponseDto createdOrder = orderService.createOrder(user, requestDto);
        return ResponseEntity.ok(createdOrder);
    }

     //Withdraw an order that has not been picked up
    @PostMapping("/{orderId}/withdraw")
    public ResponseEntity<OrderResponseDto> withdrawOrder(
            @AuthenticationPrincipal MyUserDetails customUser,
            @PathVariable Long orderId
    ) {
        User user = customUser.getUser();
        OrderResponseDto withdrawnOrder = orderService.withdrawOrder(orderId, user);
        return ResponseEntity.ok(withdrawnOrder);
    }

    //Get all orders submitted by the user
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(
            @AuthenticationPrincipal MyUserDetails customUser
    ) {
        User user = customUser.getUser();
        List<OrderResponseDto> orders = orderService.getOrdersByCustomer(user);
        return ResponseEntity.ok(orders);
    }

    //Get details of a single order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @AuthenticationPrincipal MyUserDetails customUser,
            @PathVariable Long orderId
    ) {
        User user = customUser.getUser();
        List<OrderResponseDto> userOrders = orderService.getOrdersByCustomer(user);
        return userOrders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
