package org.example.se_project_final.controller.staff;

import org.example.se_project_final.model.Staff;
import org.example.se_project_final.model.Feedback;
import org.example.se_project_final.model.Trip;
import org.example.se_project_final.model.Booking;
import org.example.se_project_final.model.Report;
import org.example.se_project_final.model.Issue;
import org.example.se_project_final.service.StaffService;
import org.example.se_project_final.service.FeedbackService;
import org.example.se_project_final.service.TripService;
import org.example.se_project_final.service.BookingService;
import org.example.se_project_final.service.AnalyticsService;
import org.example.se_project_final.service.ReportService;
import org.example.se_project_final.service.IssueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
public class StaffWebController {

    private final StaffService staffService;
    private final FeedbackService feedbackService;
    private final TripService tripService;
    private final BookingService bookingService;
    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    private final IssueService issueService;

    public StaffWebController(StaffService staffService, FeedbackService feedbackService, TripService tripService, BookingService bookingService, AnalyticsService analyticsService, ReportService reportService, IssueService issueService) {
        this.staffService = staffService;
        this.feedbackService = feedbackService;
        this.tripService = tripService;
        this.bookingService = bookingService;
        this.analyticsService = analyticsService;
        this.reportService = reportService;
        this.issueService = issueService;
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

    // Admin Captain Management
    @GetMapping("/staff/admin/add-captain")
    public String addCaptainPage(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) {
            return "redirect:/staff/login";
        }
        if (!"admin".equalsIgnoreCase(staff.getRole())) {
            return "redirect:/staff/dashboard";
        }
        
        model.addAttribute("staff", staff);
        return "staff/admin-add-captain";
    }

    @PostMapping("/staff/admin/save-captain")
    @ResponseBody
    public String saveCaptain(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             HttpSession session) {
        try {
            Staff staff = (Staff) session.getAttribute("loggedInStaff");
            if (staff == null || !"admin".equalsIgnoreCase(staff.getRole())) {
                return "error: Unauthorized";
            }
            
            // Create new captain (data only, no login access yet)
            Staff newCaptain = new Staff();
            newCaptain.setName(name);
            newCaptain.setEmail(email);
            newCaptain.setPassword(password);
            newCaptain.setRole("captain");
            
            Staff savedCaptain = staffService.createStaff(newCaptain);
            System.out.println("New captain added: " + savedCaptain.getName());
            
            return "success";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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

}



