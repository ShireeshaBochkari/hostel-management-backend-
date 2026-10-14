package com.hostel.hostel_management.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.ComplaintRepository;
import com.hostel.hostel_management.repository.NoticeRepository;
import com.hostel.hostel_management.repository.PaymentRepository;
import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.repository.VisitorRepository;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PaymentRepository paymentRepository;
    private final ComplaintRepository complaintRepository;
    private final VisitorRepository visitorRepository;
    private final NoticeRepository noticeRepository;

    public DashboardService(
            UserRepository userRepository,
            RoomRepository roomRepository,
            PaymentRepository paymentRepository,
            ComplaintRepository complaintRepository,
            VisitorRepository visitorRepository,
            NoticeRepository noticeRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.paymentRepository = paymentRepository;
        this.complaintRepository = complaintRepository;
        this.visitorRepository = visitorRepository;
        this.noticeRepository = noticeRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> staffStats() {
        var rooms = roomRepository.findAll();
        int capacity = rooms.stream().mapToInt(Room::getCapacity).sum();
        int occupied = rooms.stream().mapToInt(Room::getOccupied).sum();
        BigDecimal feesCollected = paymentRepository.findAll().stream()
                .map(payment -> payment.getAmount() == null ? BigDecimal.ZERO : payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", userRepository.countByRole(Role.HOSTELER));
        stats.put("totalRooms", rooms.size());
        stats.put("availableRooms", roomRepository.findByStatus("AVAILABLE").size());
        stats.put("totalCapacity", capacity);
        stats.put("occupiedBeds", occupied);
        stats.put("feesCollected", feesCollected);
        stats.put("openComplaints", complaintRepository.count());
        stats.put("visitors", visitorRepository.count());
        stats.put("notices", noticeRepository.count());
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> studentStats(User student) {
        User current = userRepository.findById(student.getId()).orElse(student);
        Map<String, Object> stats = new HashMap<>();
        boolean hasRoom = current.getRoom() != null;
        stats.put("hasRoom", hasRoom);
        stats.put("roomNumber", hasRoom ? current.getRoom().getRoomNumber() : "Not Assigned");
        if (hasRoom) {
            stats.put("roomId", current.getRoom().getId());
            stats.put("rentPerPerson", current.getRoom().getRentPerPerson());
            stats.put("capacity", current.getRoom().getCapacity());
            stats.put("occupied", current.getRoom().getOccupied());
            stats.put("availableBeds", current.getRoom().getAvailableBeds());
            stats.put("roomStatus", current.getRoom().getStatus());
        } else {
            stats.put("rentPerPerson", BigDecimal.ZERO);
        }
        stats.put("payments", paymentRepository.findByHostellerId(current.getId()).size());
        stats.put("complaints", complaintRepository.findByStudent_IdOrderByCreatedAtDesc(current.getId()).size());
        stats.put("visitors", visitorRepository.findByStudent_IdOrderByCreatedAtDesc(current.getId()).size());
        stats.put("notices", noticeRepository.count());
        return stats;
    }
}
