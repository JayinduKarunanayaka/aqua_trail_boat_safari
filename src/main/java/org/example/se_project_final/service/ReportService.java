package org.example.se_project_final.service;

import org.example.se_project_final.model.Report;
import org.example.se_project_final.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportsByCreator(String createdBy) {
        return reportRepository.findByCreatedByOrderByCreatedDateDesc(createdBy);
    }

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public Report createReport(String title, String content, String createdBy, String reportType) {
        Report report = new Report(title, content, createdBy, reportType);
        return reportRepository.save(report);
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}