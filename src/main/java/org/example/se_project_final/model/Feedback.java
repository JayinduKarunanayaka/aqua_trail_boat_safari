package org.example.se_project_final.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "feedback_text", length = 1000)
    private String feedbackText;
    
    private int rating; // 1-5 stars
    
    @Column(name = "feedback_date")
    private LocalDateTime feedbackDate;
    
    private String status; // "active", "hidden"

    public Feedback() {}

    public Feedback(String customerName, String customerEmail, String feedbackText, int rating, LocalDateTime feedbackDate, String status) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.feedbackText = feedbackText;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getFeedbackText() { return feedbackText; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public LocalDateTime getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDateTime feedbackDate) { this.feedbackDate = feedbackDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}