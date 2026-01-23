package com.example.DroneApp.ControllerTest;

import com.example.DroneApp.Controller.AdminController;
import com.example.DroneApp.Dto.DroneResponseDto;
import com.example.DroneApp.Dto.OrderRequestDto;
import com.example.DroneApp.Dto.OrderResponseDto;
import com.example.DroneApp.Service.DroneService;
import com.example.DroneApp.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private OrderService orderService;

    @Mock
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        OrderResponseDto order1 = new OrderResponseDto();
        order1.setOrderId(1L);
        OrderResponseDto order2 = new OrderResponseDto();
        order2.setOrderId(2L);

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        ResponseEntity<List<OrderResponseDto>> response = adminController.getAllOrders();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testUpdateOrderLocation() {
        Long orderId = 1L;
        OrderRequestDto dto = new OrderRequestDto();
        OrderResponseDto updatedOrder = new OrderResponseDto();
        updatedOrder.setOrderId(orderId);

        when(orderService.updateOrderLocation(orderId, dto)).thenReturn(updatedOrder);

        ResponseEntity<OrderResponseDto> response = adminController.updateOrderLocation(orderId, dto);

        assertNotNull(response);
        assertEquals(orderId, response.getBody().getOrderId());
        verify(orderService, times(1)).updateOrderLocation(orderId, dto);
    }

    @Test
    void testGetAllDrones() {
        DroneResponseDto drone1 = new DroneResponseDto();
        drone1.setDroneId(1L);
        DroneResponseDto drone2 = new DroneResponseDto();
        drone2.setDroneId(2L);

        when(droneService.getAllDrones()).thenReturn(Arrays.asList(drone1, drone2));

        ResponseEntity<List<DroneResponseDto>> response = adminController.getAllDrones();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(droneService, times(1)).getAllDrones();
    }

    @Test
    void testMarkDroneBroken() {
        Long droneId = 1L;
        DroneResponseDto drone = new DroneResponseDto();
        drone.setDroneId(droneId);

        when(droneService.markBroken(droneId)).thenReturn(drone);

        ResponseEntity<DroneResponseDto> response = adminController.markDroneBroken(droneId);

        assertNotNull(response);
        assertEquals(droneId, response.getBody().getDroneId());
        verify(droneService, times(1)).markBroken(droneId);
    }

    @Test
    void testMarkDroneFixed() {
        Long droneId = 1L;
        DroneResponseDto drone = new DroneResponseDto();
        drone.setDroneId(droneId);

        when(droneService.markFixed(droneId)).thenReturn(drone);

        ResponseEntity<DroneResponseDto> response = adminController.markDroneFixed(droneId);

        assertNotNull(response);
        assertEquals(droneId, response.getBody().getDroneId());
        verify(droneService, times(1)).markFixed(droneId);
    }
}
