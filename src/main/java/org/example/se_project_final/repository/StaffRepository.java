package org.example.se_project_final.repository;

import org.example.se_project_final.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    // Return Staff or null if not found (works with Spring Data)
    Staff findByEmail(String email);
}

