package com.hostel.hostel_management.dto;

import java.time.LocalDateTime;

import com.hostel.hostel_management.entity.Message;
import com.hostel.hostel_management.entity.User;

public class MessageResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean readByReceiver;

    private Long senderId;
    private String senderName;
    private String senderEmail;
    private String senderRole;

    private Long receiverId;
    private String receiverName;
    private String receiverEmail;
    private String receiverRole;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.readByReceiver = message.isReadByReceiver();

        User sender = message.getSender();
        User receiver = message.getReceiver();

        this.senderId = sender.getId();
        this.senderName = sender.getFullName();
        this.senderEmail = sender.getEmail();
        this.senderRole = sender.getRole().name();

        this.receiverId = receiver.getId();
        this.receiverName = receiver.getFullName();
        this.receiverEmail = receiver.getEmail();
        this.receiverRole = receiver.getRole().name();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isReadByReceiver() {
        return readByReceiver;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getReceiverRole() {
        return receiverRole;
    }
}
