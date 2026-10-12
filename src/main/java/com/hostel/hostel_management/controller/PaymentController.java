package com.hostel.hostel_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.PaymentRequest;
import com.hostel.hostel_management.dto.PaymentResponse;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.service.CurrentUserService;
import com.hostel.hostel_management.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    public PaymentController(
            PaymentService paymentService,
            CurrentUserService currentUserService) {
        this.paymentService = paymentService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<PaymentResponse> recordPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                PaymentResponse.from(paymentService.recordPayment(
                        request.getHostellerId(),
                        request.getYear(),
                        request.getMonth(),
                        request.getAmount(),
                        request.getPaymentMethod(),
                        request.getNote())));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('HOSTELER')")
    public List<PaymentResponse> myPayments() {
        User student = currentUserService.requireUser();
        return paymentService.getHostellerPayments(student.getId()).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @GetMapping("/hosteller/{hostellerId}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public List<PaymentResponse> getHostellerPayments(@PathVariable Long hostellerId) {
        return paymentService.getHostellerPayments(hostellerId).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public List<PaymentResponse> getMonthlyPayments(
            @RequestParam int year,
            @RequestParam int month) {
        return paymentService.getMonthlyPayments(year, month).stream()
                .map(PaymentResponse::from)
                .toList();
    }
}
