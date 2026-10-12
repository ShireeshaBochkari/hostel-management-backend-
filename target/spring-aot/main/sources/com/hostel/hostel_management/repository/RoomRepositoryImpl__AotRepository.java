package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.entity.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link RoomRepository}.
 */
@Generated
public class RoomRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public RoomRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link RoomRepository#findByRoomNumber(java.lang.String)}.
   */
  public Optional<Room> findByRoomNumber(String roomNumber) {
    String queryString = "SELECT r FROM Room r WHERE r.roomNumber = :roomNumber";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("roomNumber", roomNumber);

    return Optional.ofNullable((Room) convertOne(query.getSingleResultOrNull(), false, Room.class));
  }

  /**
   * AOT generated implementation of {@link RoomRepository#findByStatus(java.lang.String)}.
   */
  public List<Room> findByStatus(String status) {
    String queryString = "SELECT r FROM Room r WHERE r.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<Room>) query.getResultList();
  }
}
