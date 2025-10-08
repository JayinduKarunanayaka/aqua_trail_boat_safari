package org.example.se_project_final.repository;

import org.example.se_project_final.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCreatedByOrderByCreatedDateDesc(String createdBy);
    List<Report> findAllByOrderByCreatedDateDesc();
}