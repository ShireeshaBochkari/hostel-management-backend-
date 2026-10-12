package com.hostel.hostel_management.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.hostel_management.entity.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStudent_IdOrderByCreatedAtDesc(Long studentId);

    Page<Complaint> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
