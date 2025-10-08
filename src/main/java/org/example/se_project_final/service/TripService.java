package org.example.se_project_final.service;

import org.example.se_project_final.model.Trip;
import org.example.se_project_final.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository repository;

    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    public Trip getTripById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Trip saveTrip(Trip trip) {
        return repository.save(trip);
    }

    public List<Trip> getTripsByCaptainId(Long captainId) {
        return repository.findByCaptainIdOrderByIdDesc(captainId);
    }

    public List<Trip> getTripsByCaptainName(String captainName) {
        return repository.findByCaptainNameOrderByIdDesc(captainName);
    }
}
