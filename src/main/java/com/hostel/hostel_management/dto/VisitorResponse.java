package com.hostel.hostel_management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hostel.hostel_management.entity.Visitor;

public class VisitorResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String visitorName;
    private String relation;
    private String phoneNumber;
    private String purpose;
    private LocalDate visitDate;
    private String status;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    private LocalDateTime createdAt;

    public static VisitorResponse from(Visitor visitor) {
        VisitorResponse response = new VisitorResponse();
        response.id = visitor.getId();
        response.studentId = visitor.getStudent().getId();
        response.studentName = visitor.getStudent().getFullName();
        response.visitorName = visitor.getVisitorName();
        response.relation = visitor.getRelation();
        response.phoneNumber = visitor.getPhoneNumber();
        response.purpose = visitor.getPurpose();
        response.visitDate = visitor.getVisitDate();
        response.status = visitor.getStatus().name();
        response.checkInAt = visitor.getCheckInAt();
        response.checkOutAt = visitor.getCheckOutAt();
        response.createdAt = visitor.getCreatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getRelation() {
        return relation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCheckInAt() {
        return checkInAt;
    }

    public LocalDateTime getCheckOutAt() {
        return checkOutAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
