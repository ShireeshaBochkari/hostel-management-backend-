package com.hostel.hostel_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.NoticeRequest;
import com.hostel.hostel_management.dto.NoticeResponse;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.service.CurrentUserService;
import com.hostel.hostel_management.service.NoticeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;
    private final CurrentUserService currentUserService;

    public NoticeController(
            NoticeService noticeService,
            CurrentUserService currentUserService) {
        this.noticeService = noticeService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<NoticeResponse> all() {
        return noticeService.findAll().stream()
                .map(NoticeResponse::from)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<NoticeResponse> create(@Valid @RequestBody NoticeRequest request) {
        User staff = currentUserService.requireUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NoticeResponse.from(
                        noticeService.create(staff, request.getTitle(), request.getContent())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public NoticeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody NoticeRequest request) {
        return NoticeResponse.from(
                noticeService.update(id, request.getTitle(), request.getContent()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
