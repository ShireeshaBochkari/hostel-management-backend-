package com.hostel.hostel_management.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.ComplaintRequest;
import com.hostel.hostel_management.dto.ComplaintResponse;
import com.hostel.hostel_management.dto.ComplaintUpdateRequest;
import com.hostel.hostel_management.dto.PagedResponse;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.service.ComplaintService;
import com.hostel.hostel_management.service.CurrentUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final CurrentUserService currentUserService;

    public ComplaintController(
            ComplaintService complaintService,
            CurrentUserService currentUserService) {
        this.complaintService = complaintService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HOSTELER')")
    public ResponseEntity<ComplaintResponse> create(@Valid @RequestBody ComplaintRequest request) {
        User student = currentUserService.requireUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ComplaintResponse.from(
                        complaintService.create(student, request.getTitle(), request.getDescription())));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('HOSTELER')")
    public List<ComplaintResponse> myComplaints() {
        User student = currentUserService.requireUser();
        return complaintService.myComplaints(student).stream()
                .map(ComplaintResponse::from)
                .toList();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public PagedResponse<ComplaintResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<ComplaintResponse> result = complaintService
                .allComplaints(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(ComplaintResponse::from);
        return PagedResponse.from(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ComplaintResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ComplaintUpdateRequest request) {
        return ComplaintResponse.from(
                complaintService.updateStatus(id, request.getStatus(), request.getStaffResponse()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        complaintService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
