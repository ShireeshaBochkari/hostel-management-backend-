package com.hostel.hostel_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.hostel_management.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByCreatedAtDesc();
}
