package com.hostel.hostel_management.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hostel.hostel_management.entity.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findByName(String name);
    boolean existsByName(String name);
}
