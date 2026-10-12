package com.hostel.hostel_management.dto;

import jakarta.validation.constraints.NotBlank;

public class ComplaintUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status;

    private String staffResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffResponse() {
        return staffResponse;
    }

    public void setStaffResponse(String staffResponse) {
        this.staffResponse = staffResponse;
    }
}
