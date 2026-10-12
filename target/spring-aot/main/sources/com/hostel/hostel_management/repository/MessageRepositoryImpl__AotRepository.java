package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.entity.Message;
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
 * AOT generated JPA repository implementation for {@link MessageRepository}.
 */
@Generated
public class MessageRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public MessageRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link MessageRepository#findByReceiver_IdOrderByCreatedAtAsc(java.lang.Long)}.
   */
  public List<Message> findByReceiver_IdOrderByCreatedAtAsc(Long receiverId) {
    String queryString = "SELECT m FROM Message m WHERE m.receiver.id = :receiverId ORDER BY m.createdAt asc";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("receiverId", receiverId);

    return (List<Message>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link MessageRepository#findBySender_IdOrReceiver_IdOrderByCreatedAtAsc(java.lang.Long,java.lang.Long)}.
   */
  public List<Message> findBySender_IdOrReceiver_IdOrderByCreatedAtAsc(Long senderId,
      Long receiverId) {
    String queryString = "SELECT m FROM Message m WHERE m.sender.id = :senderId OR m.receiver.id = :receiverId ORDER BY m.createdAt asc";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("senderId", senderId);
    query.setParameter("receiverId", receiverId);

    return (List<Message>) query.getResultList();
  }
}
