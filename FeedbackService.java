package org.example.se_project_final.service;

import org.example.se_project_final.model.Feedback;
import org.example.se_project_final.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getAllActiveFeedback() {
        return feedbackRepository.findByStatusOrderByFeedbackDateDesc("active");
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByFeedbackDateDesc();
    }

    public Feedback saveFeedback(Feedback feedback) {
        feedback.setFeedbackDate(LocalDateTime.now());
        feedback.setStatus("active");
        return feedbackRepository.save(feedback);
    }

    public Feedback createFeedback(String customerName, String customerEmail, String feedbackText, int rating) {
        Feedback feedback = new Feedback(customerName, customerEmail, feedbackText, rating, LocalDateTime.now(), "active");
        return feedbackRepository.save(feedback);
    }
}