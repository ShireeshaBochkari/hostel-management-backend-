package com.hostel.hostel_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hostel.hostel_management.dto.RoomAssignmentRequest;
import com.hostel.hostel_management.dto.RoomRequest;
import com.hostel.hostel_management.dto.RoomResponse;
import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms().stream()
                .map(RoomResponse::from)
                .toList();
    }

    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms() {
        return roomService.getAvailableRooms().stream()
                .map(RoomResponse::from)
                .toList();
    }

    @GetMapping("/my-room")
    @PreAuthorize("hasRole('HOSTELER')")
    public Map<String, Object> getMyRoom() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first.");
        }

        User hosteller = roomService.getHostellerByEmail(authentication.getName());

        Map<String, Object> response = new java.util.LinkedHashMap<>();
        if (hosteller.getRoom() == null) {
            response.put("assigned", false);
            response.put("message", "No room has been assigned yet.");
            return response;
        }

        Room room = hosteller.getRoom();
        response.put("assigned", true);
        response.put("hostellerId", hosteller.getId());
        response.put("hostellerName", hosteller.getFullName());
        response.put("roomId", room.getId());
        response.put("roomNumber", room.getRoomNumber());
        response.put("capacity", room.getCapacity());
        response.put("occupied", room.getOccupied());
        response.put("availableBeds", room.getAvailableBeds());
        response.put("rentPerPerson", room.getRentPerPerson());
        response.put("status", room.getStatus());
        if (room.getAmenities() != null) {
            response.put("amenities", room.getAmenities().stream()
                    .map(com.hostel.hostel_management.entity.Amenity::getName)
                    .toList());
        } else {
            response.put("amenities", java.util.List.of());
        }
        return response;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<RoomResponse> addRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                RoomResponse.from(roomService.addRoom(
                        request.getRoomNumber(),
                        request.getCapacity(),
                        request.getRentPerPerson())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public RoomResponse updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return RoomResponse.from(roomService.updateRoom(
                id,
                request.getRoomNumber(),
                request.getCapacity(),
                request.getRentPerPerson()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public Map<String, Object> assignHostellerToRoom(
            @RequestBody RoomAssignmentRequest request) {
        User hosteller = roomService.assignHostellerToRoom(
                request.getHostellerId(),
                request.getRoomId());

        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "Hosteller assigned to room successfully.");
        response.put("hostellerId", hosteller.getId());
        response.put("hostellerName", hosteller.getFullName());
        if (hosteller.getRoom() != null) {
            response.put("roomId", hosteller.getRoom().getId());
            response.put("roomNumber", hosteller.getRoom().getRoomNumber());
        }
        return response;
    }

    @PutMapping("/reassign/{hostellerId}/{newRoomId}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public Map<String, Object> reassignHosteller(
            @PathVariable Long hostellerId,
            @PathVariable Long newRoomId) {
        User hosteller = roomService.reassignHostellerToRoom(hostellerId, newRoomId);

        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "Room changed successfully.");
        response.put("hostellerId", hosteller.getId());
        response.put("hostellerName", hosteller.getFullName());
        if (hosteller.getRoom() != null) {
            response.put("roomId", hosteller.getRoom().getId());
            response.put("roomNumber", hosteller.getRoom().getRoomNumber());
        }
        return response;
    }

    @PutMapping("/unassign/{hostellerId}")
    @PreAuthorize("hasAnyRole('WARDEN', 'ADMIN')")
    public Map<String, Object> unassignHosteller(@PathVariable Long hostellerId) {
        User hosteller = roomService.unassignHostellerFromRoom(hostellerId);

        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "Hosteller unassigned from room successfully.");
        response.put("hostellerId", hosteller.getId());
        response.put("hostellerName", hosteller.getFullName());
        response.put("roomNumber", "Not Assigned");
        return response;
    }
}
