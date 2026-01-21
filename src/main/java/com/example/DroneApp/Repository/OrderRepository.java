package com.example.DroneApp.Repository;

import com.example.DroneApp.Enum.OrderStatus;
import com.example.DroneApp.Model.Drone;
import com.example.DroneApp.Model.Order;
import com.example.DroneApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders for a specific enduser
    List<Order> findByCustomer(User customer);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // Find orders assigned to a specific drone
    List<Order> findByAssignedDrone(Drone drone);

}
