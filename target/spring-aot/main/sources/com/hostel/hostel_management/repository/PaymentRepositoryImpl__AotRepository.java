package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.entity.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link PaymentRepository}.
 */
@Generated
public class PaymentRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PaymentRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PaymentRepository#findByBillingYearAndBillingMonth(int,int)}.
   */
  public List<Payment> findByBillingYearAndBillingMonth(int billingYear, int billingMonth) {
    String queryString = "SELECT p FROM Payment p WHERE p.billingYear = :billingYear AND p.billingMonth = :billingMonth";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("billingYear", billingYear);
    query.setParameter("billingMonth", billingMonth);

    return (List<Payment>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PaymentRepository#findByHostellerId(java.lang.Long)}.
   */
  public List<Payment> findByHostellerId(Long hostellerId) {
    String queryString = "SELECT p FROM Payment p WHERE p.hosteller.id = :hostellerId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("hostellerId", hostellerId);

    return (List<Payment>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PaymentRepository#findByHostellerIdAndBillingYearAndBillingMonth(java.lang.Long,int,int)}.
   */
  public List<Payment> findByHostellerIdAndBillingYearAndBillingMonth(Long hostellerId,
      int billingYear, int billingMonth) {
    String queryString = "SELECT p FROM Payment p WHERE p.hosteller.id = :hostellerId AND p.billingYear = :billingYear AND p.billingMonth = :billingMonth";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("hostellerId", hostellerId);
    query.setParameter("billingYear", billingYear);
    query.setParameter("billingMonth", billingMonth);

    return (List<Payment>) query.getResultList();
  }
}
