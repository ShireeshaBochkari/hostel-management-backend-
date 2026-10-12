package com.hostel.hostel_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.dto.ProfileUpdateRequest;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoomRepository roomRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(
            String fullName,
            String email,
            String rawPassword,
            String phoneNumber,
            String course) {
        return registerUser(fullName, email, rawPassword, phoneNumber, course, Role.HOSTELER);
    }

    public User registerUser(
            String fullName,
            String email,
            String rawPassword,
            String phoneNumber,
            String course,
            Role role) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPhoneNumber(blankToNull(phoneNumber));
        user.setCourse(blankToNull(course));
        user.setRole(role != null ? role : Role.HOSTELER);

        return userRepository.save(user);
    }

    public User registerOwner(String fullName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.WARDEN);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> pageHostellers(Pageable pageable) {
        return userRepository.findByRole(Role.HOSTELER, pageable);
    }

    @Transactional
    public User updateProfile(User user, ProfileUpdateRequest request) {
        user.setFullName(request.getFullName().trim());
        user.setPhoneNumber(blankToNull(request.getPhoneNumber()));
        user.setCourse(blankToNull(request.getCourse()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateHosteller(Long id, ProfileUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hosteller not found."));
        if (user.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException("Only hosteller accounts can be updated here.");
        }
        return updateProfile(user, request);
    }

    @Transactional
    public void deleteHosteller(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hosteller not found."));
        if (user.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException("Only hosteller accounts can be deleted here.");
        }

        Room room = user.getRoom();
        if (room != null) {
            room.setOccupied(Math.max(0, room.getOccupied() - 1));
            user.setRoom(null);
            roomRepository.save(room);
        }
        userRepository.delete(user);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
