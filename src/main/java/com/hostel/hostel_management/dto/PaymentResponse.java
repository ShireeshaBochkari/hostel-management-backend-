package com.hostel.hostel_management.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hostel.hostel_management.entity.Payment;

public class PaymentResponse {

    private Long id;
    private Long hostellerId;
    private String hostellerName;
    private int billingYear;
    private int billingMonth;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String note;

    public static PaymentResponse from(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.id = payment.getId();
        response.hostellerId = payment.getHosteller().getId();
        response.hostellerName = payment.getHosteller().getFullName();
        response.billingYear = payment.getBillingYear();
        response.billingMonth = payment.getBillingMonth();
        response.amount = payment.getAmount();
        response.paymentDate = payment.getPaymentDate();
        response.paymentMethod = payment.getPaymentMethod();
        response.note = payment.getNote();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getHostellerId() {
        return hostellerId;
    }

    public String getHostellerName() {
        return hostellerName;
    }

    public int getBillingYear() {
        return billingYear;
    }

    public int getBillingMonth() {
        return billingMonth;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNote() {
        return note;
    }
}
