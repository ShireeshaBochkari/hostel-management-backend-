
package com.hostel.hostel_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.hostel_management.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByHostellerId(Long hostellerId);

    List<Payment> findByHostellerIdAndBillingYearAndBillingMonth(
            Long hostellerId,
            int billingYear,
            int billingMonth);

    List<Payment> findByBillingYearAndBillingMonth(
            int billingYear,
            int billingMonth);
}
