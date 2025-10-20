package org.example.se_project_final.repository;

import org.example.se_project_final.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStatusOrderByFeedbackDateDesc(String status);
    List<Feedback> findAllByOrderByFeedbackDateDesc();
}