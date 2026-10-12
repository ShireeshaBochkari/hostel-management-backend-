package com.hostel.hostel_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoticeRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 160)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 4000)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
