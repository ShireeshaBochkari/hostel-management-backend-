
package com.hostel.hostel_management.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.MessageResponse;
import com.hostel.hostel_management.entity.Message;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(
            MessageService messageService,
            UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new IllegalArgumentException(
                    "User is not authenticated.");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Logged-in user not found."));
    }

    // Send a message
    @PostMapping
    public ResponseEntity<?> sendMessage(
            Authentication authentication,
            @RequestBody MessageRequest request) {

        try {
            User sender = getLoggedInUser(authentication);

            if (request.getReceiverId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Receiver ID is required."));
            }

            Message message = messageService.sendMessage(
                    sender.getId(),
                    request.getReceiverId(),
                    request.getContent());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse(message));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all messages sent or received by the logged-in user
    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(
            Authentication authentication) {

        try {
            User user = getLoggedInUser(authentication);

            List<MessageResponse> messages = messageService
                    .getConversation(user.getId())
                    .stream()
                    .map(MessageResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messages);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get messages received by the logged-in user
    @GetMapping("/inbox")
    public ResponseEntity<?> getInbox(
            Authentication authentication) {

        try {
            User user = getLoggedInUser(authentication);

            List<MessageResponse> messages = messageService
                    .getInbox(user.getId())
                    .stream()
                    .map(MessageResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messages);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get users whom the logged-in user is allowed to message
    @GetMapping("/recipients")
    public ResponseEntity<?> getRecipients(
            Authentication authentication) {

        try {
            User user = getLoggedInUser(authentication);

            List<Role> recipientRoles;

            if (user.getRole() == Role.HOSTELER) {
                recipientRoles = List.of(
                        Role.WARDEN,
                        Role.ADMIN);
            } else if (user.getRole() == Role.WARDEN
                    || user.getRole() == Role.ADMIN) {
                recipientRoles = List.of(Role.HOSTELER);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error",
                                "You are not allowed to view message recipients."));
            }

            List<Map<String, Object>> recipients = userRepository
                    .findByRoleIn(recipientRoles)
                    .stream()
                    .map(recipient -> {
                        Map<String, Object> item =
                                new java.util.LinkedHashMap<>();

                        item.put("id", recipient.getId());
                        item.put("fullName", recipient.getFullName());
                        item.put("role", recipient.getRole().name());

                        return item;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(recipients);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Request body for sending a message
    public static class MessageRequest {

        private Long receiverId;
        private String content;

        public Long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
