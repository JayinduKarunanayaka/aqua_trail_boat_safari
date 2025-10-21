package org.example.se_project_final.repository;

import org.example.se_project_final.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByCaptainIdOrderByIdDesc(Long captainId);
    List<Trip> findByCaptainNameOrderByIdDesc(String captainName);
}