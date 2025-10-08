package org.example.se_project_final.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "trip_id")
    private Long tripId;
    
    @Column(name = "trip_name")
    private String tripName;
    
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;
    
    private String status; // "confirmed", "pending", "cancelled"
    
    private double totalPrice;

    public Booking() {}

    public Booking(String customerName, String customerEmail, Long tripId, String tripName, LocalDateTime bookingDate, String status, double totalPrice) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.tripId = tripId;
        this.tripName = tripName;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}