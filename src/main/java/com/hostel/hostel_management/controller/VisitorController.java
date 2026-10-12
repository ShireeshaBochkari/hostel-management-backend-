package com.hostel.hostel_management.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.PagedResponse;
import com.hostel.hostel_management.dto.VisitorRequest;
import com.hostel.hostel_management.dto.VisitorResponse;
import com.hostel.hostel_management.dto.VisitorStatusRequest;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.entity.VisitorStatus;
import com.hostel.hostel_management.service.CurrentUserService;
import com.hostel.hostel_management.service.VisitorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final VisitorService visitorService;
    private final CurrentUserService currentUserService;

    public VisitorController(
            VisitorService visitorService,
            CurrentUserService currentUserService) {
        this.visitorService = visitorService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HOSTELER')")
    public ResponseEntity<VisitorResponse> create(@Valid @RequestBody VisitorRequest request) {
        User student = currentUserService.requireUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VisitorResponse.from(visitorService.create(student, request)));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('HOSTELER')")
    public List<VisitorResponse> myVisitors() {
        User student = currentUserService.requireUser();
        return visitorService.myVisitors(student).stream()
                .map(VisitorResponse::from)
                .toList();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public PagedResponse<VisitorResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<VisitorResponse> result = visitorService
                .allVisitors(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(VisitorResponse::from);
        return PagedResponse.from(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public VisitorResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody VisitorStatusRequest request) {
        VisitorStatus status;
        try {
            status = VisitorStatus.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid visitor status.");
        }
        return VisitorResponse.from(visitorService.updateStatus(id, status));
    }
}
