package org.example.se_project_final.service;

import org.example.se_project_final.model.Booking;
import org.example.se_project_final.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingDateDesc();
    }

    public List<Booking> getBookingsByCustomerEmail(String customerEmail) {
        return bookingRepository.findByCustomerEmailOrderByBookingDateDesc(customerEmail);
    }

    public Booking saveBooking(Booking booking) {
        booking.setBookingDate(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking createBooking(String customerName, String customerEmail, Long tripId, String tripName, double totalPrice) {
        Booking booking = new Booking(customerName, customerEmail, tripId, tripName, LocalDateTime.now(), "confirmed", totalPrice);
        return bookingRepository.save(booking);
    }
}