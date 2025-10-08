package org.example.se_project_final.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(name = "reported_by", nullable = false)
    private String reportedBy; // Captain name who reported the issue
    
    @Column(name = "captain_email")
    private String captainEmail; // Captain's email for reference
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @Column(nullable = false)
    private String status = "open"; // "open", "in_progress", "resolved"
    
    @Column(name = "priority", nullable = false)
    private String priority = "medium"; // "low", "medium", "high", "urgent"

    // Constructors
    public Issue() {}

    public Issue(String title, String description, String reportedBy, String captainEmail, String priority) {
        this.title = title;
        this.description = description;
        this.reportedBy = reportedBy;
        this.captainEmail = captainEmail;
        this.priority = priority != null ? priority : "medium";
        this.status = "open";
        this.createdDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "open";
        }
        if (priority == null) {
            priority = "medium";
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getCaptainEmail() {
        return captainEmail;
    }

    public void setCaptainEmail(String captainEmail) {
        this.captainEmail = captainEmail;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}