package com.example.DroneApp.Repository;

import com.example.DroneApp.Model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    // Find all drones that are not broken
    List<Drone> findByIsBrokenFalse();

    // Find all drones that are broken
    List<Drone> findByIsBrokenTrue();

    // Optional: find drone by name
    Drone findByName(String name);

    // Find drones that currently have no assigned order
    List<Drone> findByCurrentOrderIsNullAndIsBrokenFalse();
}
