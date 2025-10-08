package org.example.se_project_final.repository;

import org.example.se_project_final.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByReportedByOrderByCreatedDateDesc(String reportedBy);
    List<Issue> findByStatusOrderByCreatedDateDesc(String status);
    List<Issue> findByOrderByCreatedDateDesc();
}