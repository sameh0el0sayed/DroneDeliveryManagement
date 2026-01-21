package com.example.DroneApp.Model;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private boolean isBroken = false;

    @Embedded
    private Location currentLocation;

    @OneToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    private LocalDateTime lastHeartbeat;

    // Constructors
    public Drone() {}

    public Drone(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isBroken() { return isBroken; }
    public void setBroken(boolean broken) { isBroken = broken; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public Order getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }

    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
}
