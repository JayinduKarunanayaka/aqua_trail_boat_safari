package org.example.se_project_final.controller.staff;

import org.example.se_project_final.model.Staff;
import org.example.se_project_final.model.Feedback;
import org.example.se_project_final.model.Trip;
import org.example.se_project_final.model.Booking;
import org.example.se_project_final.model.Report;
import org.example.se_project_final.model.Issue;
import org.example.se_project_final.model.Boat;
import org.example.se_project_final.model.Offer;
import org.example.se_project_final.service.StaffService;
import org.example.se_project_final.service.FeedbackService;
import org.example.se_project_final.service.TripService;
import org.example.se_project_final.service.BookingService;
import org.example.se_project_final.service.AnalyticsService;
import org.example.se_project_final.service.ReportService;
import org.example.se_project_final.service.IssueService;
import org.example.se_project_final.service.BoatService;
import org.example.se_project_final.service.OfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StaffWebController {

    private final StaffService staffService;
    private final FeedbackService feedbackService;
    private final TripService tripService;
    private final BookingService bookingService;
    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    private final IssueService issueService;
    private final BoatService boatService;
    private final OfferService offerService;

    public StaffWebController(StaffService staffService, FeedbackService feedbackService, TripService tripService, BookingService bookingService, AnalyticsService analyticsService, ReportService reportService, IssueService issueService, BoatService boatService, OfferService offerService) {
        this.staffService = staffService;
        this.feedbackService = feedbackService;
        this.tripService = tripService;
        this.bookingService = bookingService;
        this.analyticsService = analyticsService;
        this.reportService = reportService;
        this.issueService = issueService;
        this.boatService = boatService;
        this.offerService = offerService;
    }

    // Staff login page
    @GetMapping("/staff")
    public String staffPortal() {
        return "redirect:/staff/login";
    }

    @GetMapping("/staff/login")
    public String staffLoginPage() {
        return "staff/login";
    }

    // Handle staff login
    @PostMapping("/staff/login")
    public String staffLogin(@RequestParam String email, @RequestParam String password, 
                           HttpSession session, Model model) {
        try {
            System.out.println("Login attempt for email: " + email);
            Staff staff = staffService.getByEmail(email);
            
            if (staff != null) {
                System.out.println("Found staff: " + staff.getName() + ", Role: " + staff.getRole());
                System.out.println("Password check: " + staff.getPassword().equals(password));
                
                if (staff.getPassword().equals(password)) {
                    // Store staff in session
                    session.setAttribute("loggedInStaff", staff);
                    System.out.println("Login successful for: " + staff.getName());
                    
                    // Redirect based on role
                    String role = staff.getRole().toLowerCase();
                    switch (role) {
                        case "admin":
                            return "redirect:/staff/admin";
                        case "booking":
                            return "redirect:/staff/booking";
                        case "manager":
                            return "redirect:/staff/manager";
                        case "marketing":
                            return "redirect:/staff/marketing";
                        case "captain":
                            return "redirect:/staff/captain";
                        default:
                            return "redirect:/staff/dashboard";
                    }
                } else {
                    System.out.println("Password mismatch for: " + email);
                }
            } else {
                System.out.println("No staff found with email: " + email);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        
        model.addAttribute("loginError", true);
        return "staff/login";
    }

    // General staff dashboard (fallback)
    @GetMapping("/staff/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        model.addAttribute("staff", staff);
        return "staff/dashboard";
    }

    @GetMapping("/staff/admin")
    public String adminPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("staff", staff);
        return "staff/admin-dashboard";
    }

    @GetMapping("/staff/booking")
    public String bookingPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"booking".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("staff", staff);
        return "staff/booking-dashboard";
    }

    @GetMapping("/staff/manager")
    public String managerPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"manager".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("staff", staff);
        return "staff/manager-dashboard";
    }

    @GetMapping("/staff/marketing")
    public String marketingPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("staff", staff);
        return "staff/marketing-dashboard";
    }

    @GetMapping("/staff/captain")
    public String captainPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("staff", staff);
        return "staff/captain-dashboard";
    }

    @GetMapping("/staff/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/staff/login";
    }

    // Manager feedback management
    @GetMapping("/staff/manager/feedbacks")
    public String managerFeedbacks(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"manager".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        model.addAttribute("staff", staff);
        model.addAttribute("feedbacks", feedbacks);
        return "staff/manager-feedbacks";
    }

    @PostMapping("/staff/manager/reply-feedback")
    @ResponseBody
    public String replyToFeedback(@RequestParam Long feedbackId,
                                @RequestParam String replyMessage,
                                HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"manager".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            // For now, just log the reply (you can extend this to save replies to database)
            System.out.println("Manager " + staff.getName() + " replied to feedback " + feedbackId + ": " + replyMessage);
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error replying to feedback: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Manager Ongoing Trips Viewing
    @GetMapping("/staff/manager/ongoing-trips")
    public String managerOngoingTrips(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"manager".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            // Get all trips with "started" status
            List<Trip> ongoingTrips = tripService.getAllTrips()
                    .stream()
                    .filter(trip -> "started".equals(trip.getStatus()))
                    .collect(Collectors.toList());
            
            System.out.println("Found " + ongoingTrips.size() + " ongoing trips");
            
            model.addAttribute("staff", staff);
            model.addAttribute("ongoingTrips", ongoingTrips);
            model.addAttribute("tripsCount", ongoingTrips.size());
            
            return "staff/manager-ongoing-trips";
        } catch (Exception e) {
            System.out.println("Error loading ongoing trips: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to load ongoing trips: " + e.getMessage());
            return "staff/manager-dashboard";
        }
    }

    // Booking Officer Management
    @GetMapping("/staff/booking/create-trip")
    public String createTripPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"booking".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/booking-create-trip";
    }

    @PostMapping("/staff/booking/save-trip")
    @ResponseBody
    public String saveTrip(@RequestParam String tripName,
                          @RequestParam String description,
                          @RequestParam String duration,
                          @RequestParam double price,
                          @RequestParam String imageUrl,
                          @RequestParam(required = false) String captain,
                          @RequestParam(required = false) String boat,
                          HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"booking".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            System.out.println("Creating new trip by " + staff.getName());
            System.out.println("Trip Name: " + tripName);
            System.out.println("Duration: " + duration);
            System.out.println("Price: " + price);
            System.out.println("Captain: " + captain);
            System.out.println("Boat: " + boat);
            
            // Create and save trip
            Trip trip = new Trip();
            trip.setTripName(tripName);
            trip.setDescription(description);
            trip.setDuration(duration);
            trip.setPrice(price);
            trip.setImageUrl(imageUrl);
            trip.setStatus("active");
            
            // Set captain assignment if provided
            if (captain != null && !captain.trim().isEmpty()) {
                trip.setCaptainName(captain);
                // Find captain ID (assuming Tom Captain has ID 5 from our database)
                if ("Tom Captain".equals(captain)) {
                    trip.setCaptainId(5L);
                }
            }
            
            // Set boat assignment if provided
            if (boat != null && !boat.trim().isEmpty()) {
                trip.setBoatName(boat);
            }
            
            Trip savedTrip = tripService.saveTrip(trip);
            System.out.println("Trip created successfully with ID: " + savedTrip.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating trip: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/staff/booking/view-bookings")
    public String viewBookingsPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"booking".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("staff", staff);
        model.addAttribute("trips", trips);
        return "staff/booking-view-bookings";
    }

    @GetMapping("/staff/booking/trip-bookings")
    @ResponseBody
    public List<Booking> getTripBookings(@RequestParam Long tripId, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null || !"booking".equalsIgnoreCase(staff.getRole())) {
            return null;
        }
        
        // For demo, return all bookings (in real app, filter by tripId)
        return bookingService.getAllBookings();
    }

    @GetMapping("/staff/booking/manage-trips")
    public String bookingManageTripsPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"booking".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            List<Trip> allTrips = tripService.getAllTrips();
            List<Staff> captains = staffService.getAllStaff().stream()
                    .filter(s -> "captain".equalsIgnoreCase(s.getRole()))
                    .collect(java.util.stream.Collectors.toList());
            
            model.addAttribute("staff", staff);
            model.addAttribute("trips", allTrips);
            model.addAttribute("captains", captains);
            model.addAttribute("tripsCount", allTrips.size());
            
            return "staff/booking-manage-trips";
        } catch (Exception e) {
            System.out.println("Error loading trips: " + e.getMessage());
            model.addAttribute("error", "Unable to load trips: " + e.getMessage());
            return "staff/booking-dashboard";
        }
    }

    @PostMapping("/staff/booking/update-trip")
    @ResponseBody
    public String bookingUpdateTrip(@RequestParam Long tripId,
                            @RequestParam String tripName,
                            @RequestParam String description,
                            @RequestParam String duration,
                            @RequestParam double price,
                            @RequestParam String imageUrl,
                            @RequestParam(required = false) String captainName,
                            @RequestParam(required = false) String boatName,
                            @RequestParam String status,
                            HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"booking".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                return "error: Trip not found";
            }
            
            // Update trip details
            trip.setTripName(tripName);
            trip.setDescription(description);
            trip.setDuration(duration);
            trip.setPrice(price);
            trip.setImageUrl(imageUrl);
            trip.setStatus(status);
            
            // Update captain assignment
            if (captainName != null && !captainName.trim().isEmpty() && !"none".equals(captainName)) {
                trip.setCaptainName(captainName);
                if ("Tom Captain".equals(captainName)) {
                    trip.setCaptainId(5L);
                }
            } else {
                trip.setCaptainName(null);
                trip.setCaptainId(null);
            }
            
            // Update boat assignment
            if (boatName != null && !boatName.trim().isEmpty() && !"none".equals(boatName)) {
                trip.setBoatName(boatName);
            } else {
                trip.setBoatName(null);
            }
            
            tripService.saveTrip(trip);
            System.out.println("Trip updated by booking officer: " + trip.getTripName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error updating trip: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/staff/booking/delete-trip")
    @ResponseBody
    public String bookingDeleteTrip(@RequestParam Long tripId, HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"booking".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                return "error: Trip not found";
            }
            
            tripService.deleteTrip(tripId);
            System.out.println("Trip deleted by booking officer: " + trip.getTripName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error deleting trip: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Debug endpoint to check database
    @GetMapping("/staff/debug")
    public String debugStaff(Model model) {
        try {
            // Get all staff from database
            java.util.List<Staff> allStaff = staffService.getAllStaff();
            model.addAttribute("staffList", allStaff);
            model.addAttribute("staffCount", allStaff.size());
            
            // Try to create test data if none exists
            if (allStaff.isEmpty()) {
                System.out.println("No staff found in database. Creating test data...");
                createTestStaff();
                allStaff = staffService.getAllStaff();
                model.addAttribute("staffList", allStaff);
                model.addAttribute("staffCount", allStaff.size());
                model.addAttribute("message", "Test data created successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return "staff/debug";
    }

    // Helper method to create test staff
    private void createTestStaff() {
        try {
            Staff admin = new Staff();
            admin.setName("John Admin");
            admin.setEmail("admin@company.com");
            admin.setRole("admin");
            admin.setPassword("admin123");
            staffService.createStaff(admin);

            Staff manager = new Staff();
            manager.setName("Jayindu Karunanayaka");
            manager.setEmail("manager@company.com");
            manager.setRole("manager");
            manager.setPassword("manager123");
            staffService.createStaff(manager);

            Staff booking = new Staff();
            booking.setName("Dileka Samaranayaka");
            booking.setEmail("booking@company.com");
            booking.setRole("booking");
            booking.setPassword("booking123");
            staffService.createStaff(booking);

            Staff marketing = new Staff();
            marketing.setName("Deepana Gunasinghe");
            marketing.setEmail("marketing@company.com");
            marketing.setRole("marketing");
            marketing.setPassword("marketing123");
            staffService.createStaff(marketing);

            Staff captain = new Staff();
            captain.setName("Tom Captain");
            captain.setEmail("captain@company.com");
            captain.setRole("captain");
            captain.setPassword("captain123");
            staffService.createStaff(captain);

            System.out.println("Test staff data created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating test staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Marketing Executive Analytics
    @GetMapping("/staff/marketing/analytics")
    public String marketingAnalytics(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        Map<String, Object> analyticsData = analyticsService.getAnalyticsData();
        model.addAttribute("staff", staff);
        model.addAttribute("analytics", analyticsData);
        return "staff/marketing-analytics";
    }

    // Marketing Executive Reports
    @GetMapping("/staff/marketing/reports")
    public String marketingReports(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Report> reports = reportService.getReportsByCreator(staff.getName());
        model.addAttribute("staff", staff);
        model.addAttribute("reports", reports);
        return "staff/marketing-reports";
    }

    @GetMapping("/staff/marketing/create-report")
    public String createReportPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/marketing-create-report";
    }

    @PostMapping("/staff/marketing/save-report")
    @ResponseBody
    public String saveReport(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String reportType,
                           HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Report report = reportService.createReport(title, content, staff.getName(), reportType);
            System.out.println("Report created successfully with ID: " + report.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // Marketing Executive Reports Details
    @GetMapping("/staff/marketing/reports-details")
    public String marketingReportsDetails(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Report> allReports = reportService.getAllReports();
        model.addAttribute("staff", staff);
        model.addAttribute("reports", allReports);
        return "staff/marketing-reports-details";
    }

    @GetMapping("/staff/marketing/edit-report")
    public String editReportPage(@RequestParam Long reportId, HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        Report report = reportService.getReportById(reportId);
        if (report == null) {
            return "redirect:/staff/marketing/reports-details";
        }
        
        model.addAttribute("staff", staff);
        model.addAttribute("report", report);
        return "staff/marketing-edit-report";
    }

    @PostMapping("/staff/marketing/update-report")
    @ResponseBody
    public String updateReport(@RequestParam Long reportId,
                              @RequestParam String title,
                              @RequestParam String content,
                              @RequestParam String reportType,
                              HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Report report = reportService.getReportById(reportId);
            if (report == null) {
                return "error: Report not found";
            }
            
            // Update report details
            report.setTitle(title);
            report.setContent(content);
            report.setReportType(reportType);
            
            reportService.saveReport(report);
            System.out.println("Report updated successfully with ID: " + report.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error updating report: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/staff/marketing/delete-report")
    @ResponseBody
    public String deleteReport(@RequestParam Long reportId, HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Report report = reportService.getReportById(reportId);
            if (report == null) {
                return "error: Report not found";
            }
            
            reportService.deleteReport(reportId);
            System.out.println("Report deleted successfully with ID: " + reportId);
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error deleting report: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // Captain Issue Reporting
    @GetMapping("/staff/captain/report-issue")
    public String reportIssuePage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/captain-report-issue";
    }

    @PostMapping("/staff/captain/submit-issue")
    @ResponseBody
    public String submitIssue(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String priority,
                             HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"captain".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Issue issue = issueService.createIssue(title, description, staff.getName(), staff.getEmail(), priority);
            System.out.println("Issue created successfully by " + staff.getName() + " with ID: " + issue.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating issue: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/staff/captain/start-trip")
    public String startTrip(@RequestParam Long tripId, HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                model.addAttribute("error", "Trip not found");
                return captainMyTrips(session, model);
            }
            
            // Check if this trip is assigned to the current captain
            if (!staff.getName().equals(trip.getCaptainName())) {
                model.addAttribute("error", "You are not authorized to start this trip");
                return captainMyTrips(session, model);
            }
            
            // Update trip status to "started"
            trip.setStatus("started");
            tripService.saveTrip(trip);
            
            System.out.println("Trip started by captain " + staff.getName() + ": " + trip.getTripName());
            
            // Redirect back to the trips page
            return "redirect:/staff/captain/my-trips";
        } catch (Exception e) {
            System.out.println("Error starting trip: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to start trip: " + e.getMessage());
            return captainMyTrips(session, model);
        }
    }

    @PostMapping("/staff/captain/end-trip")
    public String endTrip(@RequestParam Long tripId, HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                model.addAttribute("error", "Trip not found");
                return captainMyTrips(session, model);
            }
            
            // Check if this trip is assigned to the current captain
            if (!staff.getName().equals(trip.getCaptainName())) {
                model.addAttribute("error", "You are not authorized to end this trip");
                return captainMyTrips(session, model);
            }
            
            // Check if the trip is in "started" status
            if (!"started".equals(trip.getStatus())) {
                model.addAttribute("error", "Trip is not in started status");
                return captainMyTrips(session, model);
            }
            
            // Update trip status to "ended"
            trip.setStatus("ended");
            tripService.saveTrip(trip);
            
            System.out.println("Trip ended by captain " + staff.getName() + ": " + trip.getTripName());
            
            // Redirect back to the trips page
            return "redirect:/staff/captain/my-trips";
        } catch (Exception e) {
            System.out.println("Error ending trip: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to end trip: " + e.getMessage());
            return captainMyTrips(session, model);
        }
    }

    // Manager Issue Viewing
    @GetMapping("/staff/manager/issues")
    public String managerIssues(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            System.out.println("No staff in session, redirecting to login");
            return "redirect:/staff/login";
        }
        if (!"manager".equalsIgnoreCase(staff.getRole())) {
            System.out.println("Staff role is not manager: " + staff.getRole());
            return "redirect:/staff/dashboard";
        }
        
        try {
            List<Issue> issues = issueService.getAllIssues();
            System.out.println("Retrieved " + issues.size() + " issues from database");
            
            // Calculate statistics
            long openIssuesCount = issues.stream().filter(issue -> "open".equals(issue.getStatus())).count();
            long inProgressIssuesCount = issues.stream().filter(issue -> "in_progress".equals(issue.getStatus())).count();
            long resolvedIssuesCount = issues.stream().filter(issue -> "resolved".equals(issue.getStatus())).count();
            long totalIssuesCount = issues.size();
            
            System.out.println("Issue stats - Open: " + openIssuesCount + ", In Progress: " + inProgressIssuesCount + ", Resolved: " + resolvedIssuesCount);
            
            model.addAttribute("staff", staff);
            model.addAttribute("issues", issues);
            model.addAttribute("openIssuesCount", openIssuesCount);
            model.addAttribute("inProgressIssuesCount", inProgressIssuesCount);
            model.addAttribute("resolvedIssuesCount", resolvedIssuesCount);
            model.addAttribute("totalIssuesCount", totalIssuesCount);
            
            System.out.println("Returning manager-issues template");
            return "staff/manager-issues-simple";
        } catch (Exception e) {
            System.out.println("Error in managerIssues: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to load issues: " + e.getMessage());
            return "staff/manager-dashboard";
        }
    }

    @PostMapping("/staff/manager/update-issue-status")
    @ResponseBody
    public String updateIssueStatus(@RequestParam Long issueId,
                                   @RequestParam String newStatus,
                                   HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"manager".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Issue updatedIssue = issueService.updateIssueStatus(issueId, newStatus);
            if (updatedIssue != null) {
                System.out.println("Issue " + issueId + " status updated to " + newStatus + " by " + staff.getName());
                return "success";
            } else {
                return "error: Issue not found";
            }
        } catch (Exception e) {
            System.out.println("Error updating issue status: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }



    // Captain Trip Viewing
    @GetMapping("/staff/captain/my-trips")
    public String captainMyTrips(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            // Get trips assigned to this captain
            List<Trip> assignedTrips = tripService.getTripsByCaptainName(staff.getName());
            System.out.println("Found " + assignedTrips.size() + " trips assigned to " + staff.getName());
            
            model.addAttribute("staff", staff);
            model.addAttribute("assignedTrips", assignedTrips);
            model.addAttribute("tripsCount", assignedTrips.size());
            
            return "staff/captain-my-trips";
        } catch (Exception e) {
            System.out.println("Error loading captain trips: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to load assigned trips: " + e.getMessage());
            return "staff/captain-dashboard";
        }
    }

    // Captain Completed Trips Viewing
    @GetMapping("/staff/captain/completed-trips")
    public String captainCompletedTrips(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"captain".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            // Get completed trips (ended) assigned to this captain
            List<Trip> completedTrips = tripService.getTripsByCaptainName(staff.getName())
                    .stream()
                    .filter(trip -> "ended".equals(trip.getStatus()))
                    .collect(Collectors.toList());
            
            System.out.println("Found " + completedTrips.size() + " completed trips for " + staff.getName());
            
            model.addAttribute("staff", staff);
            model.addAttribute("completedTrips", completedTrips);
            model.addAttribute("tripsCount", completedTrips.size());
            
            return "staff/captain-completed-trips";
        } catch (Exception e) {
            System.out.println("Error loading completed trips: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to load completed trips: " + e.getMessage());
            return "staff/captain-dashboard";
        }
    }

    // Admin Staff Management
    @GetMapping("/staff/admin/add-staff")
    public String addStaffPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/admin-add-staff";
    }

    @PostMapping("/staff/admin/save-staff")
    @ResponseBody
    public String saveStaff(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role,
                           HttpSession session) {
        try {
            Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");
            if (loggedInStaff == null || !"admin".equalsIgnoreCase(loggedInStaff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Validate role
            if (!isValidRole(role)) {
                return "error: Invalid role specified";
            }
            
            // Create new staff member
            Staff newStaff = new Staff();
            newStaff.setName(name);
            newStaff.setEmail(email);
            newStaff.setPassword(password);
            newStaff.setRole(role);
            
            Staff savedStaff = staffService.createStaff(newStaff);
            System.out.println("New staff member added: " + savedStaff.getName() + " with role: " + savedStaff.getRole());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }
    
    // Helper method to validate roles
    private boolean isValidRole(String role) {
        return "admin".equalsIgnoreCase(role) || 
               "captain".equalsIgnoreCase(role) || 
               "booking".equalsIgnoreCase(role) || 
               "manager".equalsIgnoreCase(role) || 
               "marketing".equalsIgnoreCase(role);
    }
    
    // Keep the existing captain methods for backward compatibility
    @GetMapping("/staff/admin/add-captain")
    public String addCaptainPage(HttpSession session, Model model) {
        return addStaffPage(session, model);
    }

    @PostMapping("/staff/admin/save-captain")
    @ResponseBody
    public String saveCaptain(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             HttpSession session) {
        return saveStaff(name, email, password, "captain", session);
    }

    // Admin Manage Users
    @GetMapping("/staff/admin/manage-users")
    public String manageUsersPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Staff> allStaff = staffService.getAllStaff();
        model.addAttribute("staff", staff);
        model.addAttribute("allStaff", allStaff);
        return "staff/admin-manage-users";
    }

    @GetMapping("/staff/admin/edit-user")
    public String editUserPage(@RequestParam Long staffId, HttpSession session, Model model) {
        Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");
        if (loggedInStaff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(loggedInStaff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        Staff staffToEdit = staffService.getStaffById(staffId);
        if (staffToEdit == null) {
            return "redirect:/staff/admin/manage-users";
        }
        
        model.addAttribute("staff", loggedInStaff);
        model.addAttribute("staffToEdit", staffToEdit);
        return "staff/admin-edit-user";
    }

    @PostMapping("/staff/admin/update-user")
    @ResponseBody
    public String updateUser(@RequestParam Long staffId,
                           @RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String role,
                           @RequestParam String password,
                           HttpSession session) {
        try {
            Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");
            if (loggedInStaff == null || !"admin".equalsIgnoreCase(loggedInStaff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Validate role
            if (!isValidRole(role)) {
                return "error: Invalid role specified";
            }
            
            Staff updatedStaff = new Staff();
            updatedStaff.setName(name);
            updatedStaff.setEmail(email);
            updatedStaff.setRole(role);
            updatedStaff.setPassword(password);
            
            Staff savedStaff = staffService.updateStaff(staffId, updatedStaff);
            if (savedStaff != null) {
                System.out.println("Staff member updated: " + savedStaff.getName());
                return "success";
            } else {
                return "error: Staff member not found";
            }
        } catch (Exception e) {
            System.out.println("Error updating staff: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/staff/admin/delete-user")
    @ResponseBody
    public String deleteUser(@RequestParam Long staffId, HttpSession session) {
        try {
            Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");
            if (loggedInStaff == null || !"admin".equalsIgnoreCase(loggedInStaff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Prevent self-deletion
            if (loggedInStaff.getId().equals(staffId)) {
                return "error: You cannot delete yourself";
            }
            
            Staff staffToDelete = staffService.getStaffById(staffId);
            if (staffToDelete == null) {
                return "error: Staff member not found";
            }
            
            staffService.deleteStaff(staffId);
            System.out.println("Staff member deleted: " + staffToDelete.getName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error deleting staff: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Admin Boat Management
    @GetMapping("/staff/admin/add-boat")
    public String addBoatPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/admin-add-boat";
    }

    @PostMapping("/staff/admin/save-boat")
    @ResponseBody
    public String saveBoat(@RequestParam String name,
                          @RequestParam String type,
                          @RequestParam String description,
                          @RequestParam int capacity,
                          HttpSession session) {
        try {
            Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");
            if (loggedInStaff == null || !"admin".equalsIgnoreCase(loggedInStaff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Create new boat
            Boat newBoat = new Boat();
            newBoat.setName(name);
            newBoat.setType(type);
            newBoat.setDescription(description);
            newBoat.setCapacity(capacity);
            newBoat.setStatus("active");
            
            Boat savedBoat = boatService.createBoat(newBoat);
            System.out.println("New boat added: " + savedBoat.getName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Admin Delete Boats Management
    @GetMapping("/staff/admin/delete-boats")
    public String deleteBoatsPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Boat> boats = boatService.getAllBoats();
        model.addAttribute("staff", staff);
        model.addAttribute("boats", boats);
        return "staff/admin-delete-boats";
    }

    @PostMapping("/staff/admin/delete-boat")
    @ResponseBody
    public String deleteBoat(@RequestParam Long boatId, HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"admin".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Check if boat is assigned to any trips
            List<Trip> tripsWithBoat = tripService.getAllTrips().stream()
                    .filter(trip -> trip.getBoatName() != null && !trip.getBoatName().isEmpty())
                    .filter(trip -> {
                        Boat boat = boatService.getBoatById(boatId).orElse(null);
                        return boat != null && boat.getName().equals(trip.getBoatName());
                    })
                    .collect(java.util.stream.Collectors.toList());
            
            if (!tripsWithBoat.isEmpty()) {
                return "error: Cannot delete boat as it is assigned to " + tripsWithBoat.size() + " trip(s)";
            }
            
            boatService.deleteBoat(boatId);
            System.out.println("Boat deleted successfully with ID: " + boatId);
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error deleting boat: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Admin Trip Management
    @GetMapping("/staff/admin/manage-trips")
    public String manageTripsPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        try {
            List<Trip> allTrips = tripService.getAllTrips();
            List<Staff> captains = staffService.getAllStaff().stream()
                    .filter(s -> "captain".equalsIgnoreCase(s.getRole()))
                    .collect(java.util.stream.Collectors.toList());
            
            model.addAttribute("staff", staff);
            model.addAttribute("trips", allTrips);
            model.addAttribute("captains", captains);
            model.addAttribute("tripsCount", allTrips.size());
            
            return "staff/admin-manage-trips";
        } catch (Exception e) {
            System.out.println("Error loading trips: " + e.getMessage());
            model.addAttribute("error", "Unable to load trips: " + e.getMessage());
            return "staff/admin-dashboard";
        }
    }

    @PostMapping("/staff/admin/update-trip")
    @ResponseBody
    public String updateTrip(@RequestParam Long tripId,
                            @RequestParam String tripName,
                            @RequestParam String description,
                            @RequestParam String duration,
                            @RequestParam double price,
                            @RequestParam String imageUrl,
                            @RequestParam(required = false) String captainName,
                            @RequestParam(required = false) String boatName,
                            @RequestParam String status,
                            HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"admin".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                return "error: Trip not found";
            }
            
            // Update trip details
            trip.setTripName(tripName);
            trip.setDescription(description);
            trip.setDuration(duration);
            trip.setPrice(price);
            trip.setImageUrl(imageUrl);
            trip.setStatus(status);
            
            // Update captain assignment
            if (captainName != null && !captainName.trim().isEmpty() && !"none".equals(captainName)) {
                trip.setCaptainName(captainName);
                if ("Tom Captain".equals(captainName)) {
                    trip.setCaptainId(5L);
                }
            } else {
                trip.setCaptainName(null);
                trip.setCaptainId(null);
            }
            
            // Update boat assignment
            if (boatName != null && !boatName.trim().isEmpty() && !"none".equals(boatName)) {
                trip.setBoatName(boatName);
            } else {
                trip.setBoatName(null);
            }
            
            tripService.saveTrip(trip);
            System.out.println("Trip updated by admin: " + trip.getTripName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error updating trip: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    // Admin Captain Deletion

    // Marketing Executive Offers Management
    @GetMapping("/staff/marketing/add-offer")
    public String addOfferPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/marketing-add-offer";
    }

    @PostMapping("/staff/marketing/save-offer")
    @ResponseBody
    public String saveOffer(@RequestParam String offerDate,
                           @RequestParam String startTime,
                           @RequestParam String endTime,
                           @RequestParam String offerType,
                           @RequestParam String description,
                           HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Parse date and time
            java.time.LocalDate date = java.time.LocalDate.parse(offerDate);
            java.time.LocalTime start = java.time.LocalTime.parse(startTime);
            java.time.LocalTime end = java.time.LocalTime.parse(endTime);
            
            // Create new offer
            Offer offer = new Offer();
            offer.setOfferDate(date);
            offer.setStartTime(start);
            offer.setEndTime(end);
            offer.setOfferType(offerType);
            offer.setDescription(description);
            offer.setCreatedBy(staff.getName());
            offer.setCreatedDate(java.time.LocalDate.now());
            
            Offer savedOffer = offerService.saveOffer(offer);
            System.out.println("Offer created successfully with ID: " + savedOffer.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error creating offer: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/staff/marketing/ongoing-offers")
    public String ongoingOffersPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        List<Offer> offers = offerService.getAllOffers();
        model.addAttribute("staff", staff);
        model.addAttribute("offers", offers);
        return "staff/marketing-ongoing-offers";
    }

    @GetMapping("/staff/marketing/edit-offer")
    public String editOfferPage(@RequestParam Long offerId, HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"marketing".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        Offer offer = offerService.getOfferById(offerId);
        if (offer == null) {
            return "redirect:/staff/marketing/ongoing-offers";
        }
        
        model.addAttribute("staff", staff);
        model.addAttribute("offer", offer);
        return "staff/marketing-edit-offer";
    }

    @PostMapping("/staff/marketing/update-offer")
    @ResponseBody
    public String updateOffer(@RequestParam Long offerId,
                             @RequestParam String offerDate,
                             @RequestParam String startTime,
                             @RequestParam String endTime,
                             @RequestParam String offerType,
                             @RequestParam String description,
                             HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Parse date and time
            java.time.LocalDate date = java.time.LocalDate.parse(offerDate);
            java.time.LocalTime start = java.time.LocalTime.parse(startTime);
            java.time.LocalTime end = java.time.LocalTime.parse(endTime);
            
            // Get existing offer
            Offer offer = offerService.getOfferById(offerId);
            if (offer == null) {
                return "error: Offer not found";
            }
            
            // Update offer details
            offer.setOfferDate(date);
            offer.setStartTime(start);
            offer.setEndTime(end);
            offer.setOfferType(offerType);
            offer.setDescription(description);
            
            Offer updatedOffer = offerService.updateOffer(offerId, offer);
            System.out.println("Offer updated successfully with ID: " + updatedOffer.getId());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error updating offer: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/staff/marketing/delete-offer")
    @ResponseBody
    public String deleteOffer(@RequestParam Long offerId, HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"marketing".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            Offer offer = offerService.getOfferById(offerId);
            if (offer == null) {
                return "error: Offer not found";
            }
            
            offerService.deleteOffer(offerId);
            System.out.println("Offer deleted successfully with ID: " + offerId);
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error deleting offer: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

}
