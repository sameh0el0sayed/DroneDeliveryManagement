package com.example.DroneApp.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drone")
public class DroneController {

    @PreAuthorize("hasRole('DRONE')")
    @GetMapping("/status")
    public String status() {
        return "Drone status access granted";
    }

    @PreAuthorize("hasRole('DRONE')")
    @GetMapping("/heartbeat")
    public String heartbeat() {
        return "Drone heartbeat OK";
    }
}
