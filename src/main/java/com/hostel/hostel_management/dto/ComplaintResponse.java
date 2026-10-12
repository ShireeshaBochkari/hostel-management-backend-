package com.hostel.hostel_management.dto;

import java.time.LocalDateTime;

import com.hostel.hostel_management.entity.Complaint;

public class ComplaintResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String title;
    private String description;
    private String status;
    private String staffResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ComplaintResponse from(Complaint complaint) {
        ComplaintResponse response = new ComplaintResponse();
        response.id = complaint.getId();
        response.studentId = complaint.getStudent().getId();
        response.studentName = complaint.getStudent().getFullName();
        response.title = complaint.getTitle();
        response.description = complaint.getDescription();
        response.status = complaint.getStatus().name();
        response.staffResponse = complaint.getStaffResponse();
        response.createdAt = complaint.getCreatedAt();
        response.updatedAt = complaint.getUpdatedAt();
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getStaffResponse() {
        return staffResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
