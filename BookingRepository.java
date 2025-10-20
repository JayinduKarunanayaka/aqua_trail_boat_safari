package org.example.se_project_final.repository;

import org.example.se_project_final.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerEmailOrderByBookingDateDesc(String customerEmail);
    List<Booking> findAllByOrderByBookingDateDesc();
    
    // Analytics queries
    @Query("SELECT b.tripId, b.tripName, COUNT(b) as bookingCount FROM Booking b GROUP BY b.tripId, b.tripName ORDER BY COUNT(b) DESC")
    List<Object[]> findMostBookedTrips();
    
    @Query("SELECT b.tripId, b.tripName, COUNT(b) as bookingCount FROM Booking b GROUP BY b.tripId, b.tripName ORDER BY COUNT(b) ASC")
    List<Object[]> findLeastBookedTrips();
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.bookingDate) = MONTH(CURRENT_DATE) AND YEAR(b.bookingDate) = YEAR(CURRENT_DATE)")
    Long countBookingsThisMonth();
    
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE MONTH(b.bookingDate) = MONTH(CURRENT_DATE) AND YEAR(b.bookingDate) = YEAR(CURRENT_DATE)")
    Double getTotalRevenueThisMonth();
    
    @Query("SELECT COUNT(DISTINCT b.customerEmail) FROM Booking b")
    Long countUniqueCustomers();
}