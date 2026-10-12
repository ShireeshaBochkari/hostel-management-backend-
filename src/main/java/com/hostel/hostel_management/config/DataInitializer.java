package com.hostel.hostel_management.config;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hostel.hostel_management.entity.Amenity;
import com.hostel.hostel_management.entity.Complaint;
import com.hostel.hostel_management.entity.ComplaintStatus;
import com.hostel.hostel_management.entity.Notice;
import com.hostel.hostel_management.entity.Payment;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.StudentProfile;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.AmenityRepository;
import com.hostel.hostel_management.repository.ComplaintRepository;
import com.hostel.hostel_management.repository.NoticeRepository;
import com.hostel.hostel_management.repository.PaymentRepository;
import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final AmenityRepository amenityRepository;
    private final NoticeRepository noticeRepository;
    private final ComplaintRepository complaintRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            RoomRepository roomRepository,
            AmenityRepository amenityRepository,
            NoticeRepository noticeRepository,
            ComplaintRepository complaintRepository,
            PaymentRepository paymentRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
        this.noticeRepository = noticeRepository;
        this.complaintRepository = complaintRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Checking hostel initial demo data...");

        // 1. Seed Amenities
        Amenity wifi = getOrCreateAmenity("High-Speed Wi-Fi", "wifi");
        Amenity ac = getOrCreateAmenity("Air Conditioning", "snowflake");
        Amenity washroom = getOrCreateAmenity("Attached Washroom", "bath");
        Amenity hotWater = getOrCreateAmenity("Hot Water / Geyser", "flame");
        Amenity studyTable = getOrCreateAmenity("Study Desk & Chair", "desk");

        // 2. Seed Default Warden
        User warden;
        if (!userRepository.existsByEmail("warden@hostel.com")) {
            warden = new User();
            warden.setFullName("Chief Warden");
            warden.setEmail("warden@hostel.com");
            warden.setPassword(passwordEncoder.encode("admin123"));
            warden.setPhoneNumber("9876543210");
            warden.setCourse("Hostel Administration");
            warden.setRole(Role.WARDEN);
            warden = userRepository.save(warden);
            log.info("Created default Warden account: warden@hostel.com / admin123");
        } else {
            warden = userRepository.findByEmail("warden@hostel.com").orElse(null);
        }

        // 3. Seed Default Rooms
        Room room101 = getOrCreateRoom("101", 2, new BigDecimal("6500.00"), Set.of(wifi, ac, washroom));
        getOrCreateRoom("102", 3, new BigDecimal("4500.00"), Set.of(wifi, studyTable));
        getOrCreateRoom("103", 2, new BigDecimal("5500.00"), Set.of(wifi, hotWater));
        getOrCreateRoom("201", 1, new BigDecimal("8500.00"), Set.of(wifi, ac, washroom, hotWater, studyTable));
        getOrCreateRoom("202", 4, new BigDecimal("4000.00"), Set.of(wifi, studyTable));

        // 4. Seed Default Hosteller
        User student;
        if (!userRepository.existsByEmail("student@hostel.com")) {
            student = new User();
            student.setFullName("Aarav Patel");
            student.setEmail("student@hostel.com");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setPhoneNumber("9123456780");
            student.setCourse("B.Tech Computer Science");
            student.setRole(Role.HOSTELER);
            student.setRoom(room101);

            // Assign profile
            StudentProfile profile = new StudentProfile(
                    student,
                    "Ramesh Patel",
                    "9811223344",
                    "B+",
                    "Flat 402, Green Park Avenue, New Delhi"
            );
            student.setProfile(profile);

            student = userRepository.save(student);

            // Update room occupation
            if (room101.getOccupied() == 0) {
                room101.setOccupied(1);
                roomRepository.save(room101);
            }

            log.info("Created default Hosteller account: student@hostel.com / student123");
        } else {
            student = userRepository.findByEmail("student@hostel.com").orElse(null);
        }

        // 5. Seed Notices
        if (noticeRepository.count() == 0 && warden != null) {
            Notice n1 = new Notice();
            n1.setTitle("Hostel Gate Timings & Curfew Notice");
            n1.setContent("All hostellers are informed that the main hostel gate will close strictly at 10:00 PM. Late entry requires prior warden approval.");
            n1.setCreatedBy(warden);
            noticeRepository.save(n1);

            Notice n2 = new Notice();
            n2.setTitle("High-Speed Wi-Fi Maintenance");
            n2.setContent("Campus network maintenance is scheduled for Saturday 2:00 AM to 5:00 AM. Fiber optic lines are being upgraded to 1 Gbps.");
            n2.setCreatedBy(warden);
            noticeRepository.save(n2);

            Notice n3 = new Notice();
            n3.setTitle("Monthly Mess Committee Meeting");
            n3.setContent("The monthly mess menu revision meeting will be held this Wednesday at 6:00 PM in the dining hall. All floor representatives must attend.");
            n3.setCreatedBy(warden);
            noticeRepository.save(n3);
        }

        // 6. Seed Complaints
        if (complaintRepository.count() == 0 && student != null) {
            Complaint c1 = new Complaint();
            c1.setTitle("Water heater temperature in Room 101");
            c1.setDescription("The geyser in the bathroom takes over 30 minutes to heat up and water pressure is low.");
            c1.setStudent(student);
            c1.setStatus(ComplaintStatus.IN_PROGRESS);
            c1.setStaffResponse("Plumber has been notified and scheduled for tomorrow morning.");
            complaintRepository.save(c1);

            Complaint c2 = new Complaint();
            c2.setTitle("Wi-Fi Signal Strength in Wing A");
            c2.setDescription("Signal drops frequently around 9 PM during peak study hours.");
            c2.setStudent(student);
            c2.setStatus(ComplaintStatus.RESOLVED);
            c2.setStaffResponse("Access point firmware updated and additional repeater installed on 1st floor.");
            complaintRepository.save(c2);
        }

        // 7. Seed Payment
        if (paymentRepository.count() == 0 && student != null) {
            Payment p = new Payment();
            p.setHosteller(student);
            p.setBillingYear(2026);
            p.setBillingMonth(10);
            p.setPaymentDate(java.time.LocalDate.now());
            p.setAmount(new BigDecimal("6500.00"));
            p.setPaymentMethod("UPI / Online");
            p.setNote("October 2026 hostel room rent paid via GPay");
            paymentRepository.save(p);
        }

        log.info("Hostel demo data seeding check complete.");
    }

    private Amenity getOrCreateAmenity(String name, String icon) {
        return amenityRepository.findByName(name)
                .orElseGet(() -> amenityRepository.save(new Amenity(name, icon)));
    }

    private Room getOrCreateRoom(String roomNumber, int capacity, BigDecimal rent, Set<Amenity> amenities) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseGet(() -> {
                    Room room = new Room(roomNumber, capacity, 0, rent);
                    room.setAmenities(new HashSet<>(amenities));
                    return roomRepository.save(room);
                });
    }
}
