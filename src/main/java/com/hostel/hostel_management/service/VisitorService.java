package com.hostel.hostel_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.dto.VisitorRequest;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.entity.Visitor;
import com.hostel.hostel_management.entity.VisitorStatus;
import com.hostel.hostel_management.repository.VisitorRepository;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Transactional
    public Visitor create(User student, VisitorRequest request) {
        if (student.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException("Only hostelers can register visitors.");
        }

        Visitor visitor = new Visitor();
        visitor.setStudent(student);
        visitor.setVisitorName(request.getVisitorName().trim());
        visitor.setRelation(request.getRelation());
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setPurpose(request.getPurpose().trim());
        visitor.setVisitDate(request.getVisitDate());
        return visitorRepository.save(visitor);
    }

    @Transactional(readOnly = true)
    public List<Visitor> myVisitors(User student) {
        return visitorRepository.findByStudent_IdOrderByCreatedAtDesc(student.getId());
    }

    @Transactional(readOnly = true)
    public Page<Visitor> allVisitors(Pageable pageable) {
        return visitorRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public Visitor updateStatus(Long id, VisitorStatus status) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor record not found."));

        visitor.setStatus(status);
        if (status == VisitorStatus.APPROVED && visitor.getCheckInAt() == null) {
            visitor.setCheckInAt(LocalDateTime.now());
        }
        if (status == VisitorStatus.CHECKED_OUT) {
            visitor.setCheckOutAt(LocalDateTime.now());
        }
        return visitorRepository.save(visitor);
    }
}
