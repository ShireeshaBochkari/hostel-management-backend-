package com.hostel.hostel_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Notice;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.NoticeRepository;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public Notice create(User staff, String title, String content) {
        Notice notice = new Notice();
        notice.setCreatedBy(staff);
        notice.setTitle(title.trim());
        notice.setContent(content.trim());
        return noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public List<Notice> findAll() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Notice update(Long id, String title, String content) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found."));
        notice.setTitle(title.trim());
        notice.setContent(content.trim());
        return noticeRepository.save(notice);
    }

    @Transactional
    public void delete(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new IllegalArgumentException("Notice not found.");
        }
        noticeRepository.deleteById(id);
    }
}
