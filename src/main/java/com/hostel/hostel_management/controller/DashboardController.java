package com.hostel.hostel_management.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.service.CurrentUserService;
import com.hostel.hostel_management.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    public DashboardController(
            DashboardService dashboardService,
            CurrentUserService currentUserService) {
        this.dashboardService = dashboardService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public Map<String, Object> staffStats() {
        return dashboardService.staffStats();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('HOSTELER')")
    public Map<String, Object> studentStats() {
        User student = currentUserService.requireUser();
        return dashboardService.studentStats(student);
    }
}
