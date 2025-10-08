package org.example.se_project_final.service;

import org.example.se_project_final.repository.BookingRepository;
import org.example.se_project_final.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;

    public AnalyticsService(BookingRepository bookingRepository, TripRepository tripRepository) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
    }

    public Map<String, Object> getAnalyticsData() {
        Map<String, Object> analytics = new HashMap<>();
        
        // Most booked trip
        List<Object[]> mostBookedTrips = bookingRepository.findMostBookedTrips();
        if (!mostBookedTrips.isEmpty()) {
            Object[] topTrip = mostBookedTrips.get(0);
            analytics.put("mostBookedTripName", topTrip[1]);
            analytics.put("mostBookedTripCount", topTrip[2]);
        } else {
            analytics.put("mostBookedTripName", "No bookings yet");
            analytics.put("mostBookedTripCount", 0);
        }
        
        // Least booked trip
        List<Object[]> leastBookedTrips = bookingRepository.findLeastBookedTrips();
        if (!leastBookedTrips.isEmpty()) {
            Object[] bottomTrip = leastBookedTrips.get(0);
            analytics.put("leastBookedTripName", bottomTrip[1]);
            analytics.put("leastBookedTripCount", bottomTrip[2]);
        } else {
            analytics.put("leastBookedTripName", "No bookings yet");
            analytics.put("leastBookedTripCount", 0);
        }
        
        // Bookings this month
        Long bookingsThisMonth = bookingRepository.countBookingsThisMonth();
        analytics.put("bookingsThisMonth", bookingsThisMonth != null ? bookingsThisMonth : 0);
        
        // Revenue this month
        Double revenueThisMonth = bookingRepository.getTotalRevenueThisMonth();
        analytics.put("revenueThisMonth", revenueThisMonth != null ? revenueThisMonth : 0.0);
        
        // Total unique customers
        Long uniqueCustomers = bookingRepository.countUniqueCustomers();
        analytics.put("uniqueCustomers", uniqueCustomers != null ? uniqueCustomers : 0);
        
        // Total trips available
        long totalTrips = tripRepository.count();
        analytics.put("totalTrips", totalTrips);
        
        // Total bookings
        long totalBookings = bookingRepository.count();
        analytics.put("totalBookings", totalBookings);
        
        return analytics;
    }
}