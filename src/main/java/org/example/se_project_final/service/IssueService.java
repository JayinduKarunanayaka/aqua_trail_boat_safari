package org.example.se_project_final.service;

import org.example.se_project_final.model.Issue;
import org.example.se_project_final.repository.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {
    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public Issue createIssue(String title, String description, String reportedBy, String captainEmail, String priority) {
        Issue issue = new Issue(title, description, reportedBy, captainEmail, priority);
        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findByOrderByCreatedDateDesc();
    }

    public List<Issue> getIssuesByReporter(String reportedBy) {
        return issueRepository.findByReportedByOrderByCreatedDateDesc(reportedBy);
    }

    public List<Issue> getIssuesByStatus(String status) {
        return issueRepository.findByStatusOrderByCreatedDateDesc(status);
    }

    public Issue updateIssueStatus(Long issueId, String newStatus) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue != null) {
            issue.setStatus(newStatus);
            return issueRepository.save(issue);
        }
        return null;
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
}