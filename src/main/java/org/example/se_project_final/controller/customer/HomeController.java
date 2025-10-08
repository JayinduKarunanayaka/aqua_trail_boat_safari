package org.example.se_project_final.controller.customer;
import org.example.se_project_final.model.Booking;
import org.example.se_project_final.model.Feedback;
import org.example.se_project_final.model.Trip;
import org.example.se_project_final.service.BookingService;
import org.example.se_project_final.service.FeedbackService;
import org.example.se_project_final.service.TripService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final TripService tripService;
    private final BookingService bookingService;
    private final FeedbackService feedbackService;

    public HomeController(TripService tripService, BookingService bookingService, FeedbackService feedbackService) {
        this.tripService = tripService;
        this.bookingService = bookingService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String customerHome(Model model) {
        List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("trips", trips);
        return "user/index";  // customer landing page
    }

    @GetMapping("/trip/{id}")
    public String tripDetails(@PathVariable Long id, Model model) {
        Trip trip = tripService.getTripById(id);
        model.addAttribute("trip", trip);
        return "user/trip-details"; // single trip page
    }

    @GetMapping("/bookings")
    public String viewBookings(Model model) {
        // For demo purposes, showing all bookings
        // In a real app, you'd filter by logged-in user
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "user/bookings";
    }

    @PostMapping("/book-trip")
    @ResponseBody
    public String bookTrip(@RequestParam Long tripId, 
                          @RequestParam String customerName, 
                          @RequestParam String customerEmail) {
        try {
            System.out.println("Booking request received:");
            System.out.println("Trip ID: " + tripId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                System.out.println("Trip not found for ID: " + tripId);
                return "error: Trip not found";
            }
            
            Booking booking = bookingService.createBooking(customerName, customerEmail, tripId, trip.getTripName(), trip.getPrice());
            System.out.println("Booking created successfully with ID: " + booking.getId());
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllActiveFeedback();
        model.addAttribute("feedbacks", feedbacks);
        return "user/feedback";
    }

    @PostMapping("/submit-feedback")
    @ResponseBody
    public String submitFeedback(@RequestParam String customerName,
                               @RequestParam String customerEmail,
                               @RequestParam String feedbackText,
                               @RequestParam int rating) {
        try {
            System.out.println("Feedback submission received:");
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("Rating: " + rating);
            System.out.println("Feedback: " + feedbackText);
            
            Feedback feedback = feedbackService.createFeedback(customerName, customerEmail, feedbackText, rating);
            System.out.println("Feedback created successfully with ID: " + feedback.getId());
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating feedback: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
}
