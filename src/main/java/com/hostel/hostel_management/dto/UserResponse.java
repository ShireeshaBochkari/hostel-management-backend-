package com.hostel.hostel_management.dto;

import java.time.LocalDateTime;

import com.hostel.hostel_management.entity.User;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String course;
    private String role;
    private Long roomId;
    private String roomNumber;
    private String guardianName;
    private String guardianPhone;
    private String bloodGroup;
    private String permanentAddress;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.fullName = user.getFullName();
        response.email = user.getEmail();
        response.phoneNumber = user.getPhoneNumber();
        response.course = user.getCourse();
        response.role = user.getRole().name();
        if (user.getRoom() != null) {
            response.roomId = user.getRoom().getId();
            response.roomNumber = user.getRoom().getRoomNumber();
        }
        if (user.getProfile() != null) {
            response.guardianName = user.getProfile().getGuardianName();
            response.guardianPhone = user.getProfile().getGuardianPhone();
            response.bloodGroup = user.getProfile().getBloodGroup();
            response.permanentAddress = user.getProfile().getPermanentAddress();
        }
        response.createdAt = user.getCreatedAt();
        return response;
    }

    public String getGuardianName() { return guardianName; }
    public String getGuardianPhone() { return guardianPhone; }
    public String getBloodGroup() { return bloodGroup; }
    public String getPermanentAddress() { return permanentAddress; }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCourse() {
        return course;
    }

    public String getRole() {
        return role;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
