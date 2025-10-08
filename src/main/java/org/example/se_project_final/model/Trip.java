package org.example.se_project_final.model;

import jakarta.persistence.*;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_name")
    private String tripName;
    private String description;
    private String duration;
    private double price;
    private String status;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "captain_id")
    private Long captainId;
    @Column(name = "captain_name")
    private String captainName;
    @Column(name = "boat_name")
    private String boatName;

    public Trip() {}

    public Trip(String tripName, String description, String duration, double price, String status, String imageUrl) {
        this.tripName = tripName;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Long getCaptainId() { return captainId; }
    public void setCaptainId(Long captainId) { this.captainId = captainId; }
    public String getCaptainName() { return captainName; }
    public void setCaptainName(String captainName) { this.captainName = captainName; }
    public String getBoatName() { return boatName; }
    public void setBoatName(String boatName) { this.boatName = boatName; }
}
