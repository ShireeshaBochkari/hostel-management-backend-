
package com.hostel.hostel_management.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomService(
            RoomRepository roomRepository,
            UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // Get the logged-in hosteller using their email
    @Transactional(readOnly = true)
    public User getHostellerByEmail(String email) {

        User hosteller = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in user not found."));

        if (hosteller.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException(
                    "This endpoint is only for hostellers.");
        }

        return hosteller;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get available rooms
    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus("AVAILABLE");
    }

    // Add a new room
    @Transactional
    public Room addRoom(
            String roomNumber,
            int capacity,
            BigDecimal rentPerPerson) {

        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Room number is required.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Room capacity must be greater than zero.");
        }

        if (rentPerPerson == null
                || rentPerPerson.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Rent cannot be negative or empty.");
        }

        String normalizedRoomNumber = roomNumber.trim();

        if (roomRepository.findByRoomNumber(normalizedRoomNumber)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "This room number already exists.");
        }

        Room room = new Room(
                normalizedRoomNumber,
                capacity,
                0,
                rentPerPerson);

        return roomRepository.save(room);
    }

    // Assign a hosteller to a room
    @Transactional
    public User assignHostellerToRoom(
            Long hostellerId,
            Long roomId) {

        User hosteller = userRepository.findById(hostellerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Hosteller not found."));

        if (hosteller.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException(
                    "Only hostellers can be assigned to rooms.");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room not found."));

        if (hosteller.getRoom() != null) {
            throw new IllegalArgumentException(
                    "This hosteller already has an assigned room.");
        }

        if (room.getOccupied() >= room.getCapacity()) {
            throw new IllegalArgumentException(
                    "This room is full.");
        }

        hosteller.setRoom(room);
        room.setOccupied(room.getOccupied() + 1);

        userRepository.save(hosteller);
        roomRepository.save(room);

        return hosteller;
    }

    // Change a hosteller's assigned room
    @Transactional
    public User reassignHostellerToRoom(
            Long hostellerId,
            Long newRoomId) {

        User hosteller = userRepository.findById(hostellerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Hosteller not found."));

        if (hosteller.getRole() != Role.HOSTELER) {
            throw new IllegalArgumentException(
                    "Only hostellers can be assigned to rooms.");
        }

        Room oldRoom = hosteller.getRoom();

        if (oldRoom == null) {
            throw new IllegalArgumentException(
                    "This hosteller does not have an assigned room. "
                    + "Use normal assignment.");
        }

        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "New room not found."));

        if (oldRoom.getId().equals(newRoom.getId())) {
            throw new IllegalArgumentException(
                    "The hosteller is already assigned to this room.");
        }

        if (newRoom.getOccupied() >= newRoom.getCapacity()) {
            throw new IllegalArgumentException(
                    "The new room is full.");
        }

        oldRoom.setOccupied(oldRoom.getOccupied() - 1);
        newRoom.setOccupied(newRoom.getOccupied() + 1);

        hosteller.setRoom(newRoom);

        roomRepository.save(oldRoom);
        roomRepository.save(newRoom);
        userRepository.save(hosteller);

        return hosteller;
    }

    @Transactional
    public Room updateRoom(
            Long id,
            String roomNumber,
            int capacity,
            BigDecimal rentPerPerson) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));

        String normalized = roomNumber.trim();
        roomRepository.findByRoomNumber(normalized)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("This room number already exists.");
                });

        room.setRoomNumber(normalized);
        room.setCapacity(capacity);
        room.setRentPerPerson(rentPerPerson);
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));

        if (room.getOccupied() > 0) {
            throw new IllegalArgumentException(
                    "Cannot delete a room that still has occupants.");
        }

        roomRepository.delete(room);
    }
}
