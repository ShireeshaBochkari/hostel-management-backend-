package com.hostel.hostel_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Complaint;
import com.hostel.hostel_management.entity.ComplaintStatus;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.ComplaintRepository;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Transactional
    public Complaint create(User student, String title, String description) {
        requireHosteler(student);
        Complaint complaint = new Complaint();
        complaint.setStudent(student);
        complaint.setTitle(title.trim());
        complaint.setDescription(description.trim());
        return complaintRepository.save(complaint);
    }

    @Transactional(readOnly = true)
    public List<Complaint> myComplaints(User student) {
        return complaintRepository.findByStudent_IdOrderByCreatedAtDesc(student.getId());
    }

    @Transactional(readOnly = true)
    public Page<Complaint> allComplaints(Pageable pageable) {
        return complaintRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public Complaint updateStatus(Long id, String status, String staffResponse) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found."));

        try {
            complaint.setStatus(ComplaintStatus.valueOf(status.trim().toUpperCase()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid complaint status.");
        }

        if (staffResponse != null && !staffResponse.isBlank()) {
            complaint.setStaffResponse(staffResponse.trim());
        }
        return complaintRepository.save(complaint);
    }

    @Transactional
    public void delete(Long id) {
        if (!complaintRepository.existsById(id)) {
            throw new IllegalArgumentException("Complaint not found.");
        }
        complaintRepository.deleteById(id);
    }

    private void requireHosteler(User user) {
        if (user.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException("Only hostelers can submit complaints.");
        }
    }
}
