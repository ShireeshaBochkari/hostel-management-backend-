
package com.hostel.hostel_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.hostel_management.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender_IdOrReceiver_IdOrderByCreatedAtAsc(
            Long senderId,
            Long receiverId
    );

    List<Message> findByReceiver_IdOrderByCreatedAtAsc(Long receiverId);
}
