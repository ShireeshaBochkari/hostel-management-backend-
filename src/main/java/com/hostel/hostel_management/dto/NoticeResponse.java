package com.hostel.hostel_management.dto;

import java.time.LocalDateTime;

import com.hostel.hostel_management.entity.Notice;

public class NoticeResponse {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;

    public static NoticeResponse from(Notice notice) {
        NoticeResponse response = new NoticeResponse();
        response.id = notice.getId();
        response.title = notice.getTitle();
        response.content = notice.getContent();
        response.createdBy = notice.getCreatedBy().getFullName();
        response.createdAt = notice.getCreatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
