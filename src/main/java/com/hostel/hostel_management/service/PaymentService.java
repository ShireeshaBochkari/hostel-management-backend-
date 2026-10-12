
package com.hostel.hostel_management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Payment;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.PaymentRepository;
import com.hostel.hostel_management.repository.UserRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Payment recordPayment(
            Long hostellerId,
            int year,
            int month,
            BigDecimal amount,
            String paymentMethod,
            String note) {

        if (hostellerId == null || year < 2000
                || month < 1 || month > 12
                || amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Enter a valid hosteller, month, year and positive amount.");
        }

        User hosteller = userRepository.findById(hostellerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Hosteller not found."));

        if (hosteller.getRole() != com.hostel.hostel_management.entity.Role.HOSTELER) {
            throw new IllegalArgumentException(
                    "Payments can only be recorded for hostellers.");
        }

        Payment payment = new Payment();
        payment.setHosteller(hosteller);
        payment.setBillingYear(year);
        payment.setBillingMonth(month);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentMethod);
        payment.setNote(note);

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Payment> getHostellerPayments(Long hostellerId) {
        return paymentRepository.findByHostellerId(hostellerId);
    }

    @Transactional(readOnly = true)
    public List<Payment> getMonthlyPayments(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }

        return paymentRepository.findByBillingYearAndBillingMonth(year, month);
    }
}
