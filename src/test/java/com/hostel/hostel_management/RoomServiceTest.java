package com.hostel.hostel_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hostel.hostel_management.entity.Room;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.RoomRepository;
import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.service.RoomService;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService(roomRepository, userRepository);
    }

    @Test
    void testAddRoom_Success() {
        when(roomRepository.findByRoomNumber("301")).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room created = roomService.addRoom("301", 2, new BigDecimal("5000"));

        assertNotNull(created);
        assertEquals("301", created.getRoomNumber());
        assertEquals(2, created.getCapacity());
        assertEquals(0, created.getOccupied());
        assertEquals("AVAILABLE", created.getStatus());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testAddRoom_DuplicateFails() {
        when(roomRepository.findByRoomNumber("301")).thenReturn(Optional.of(new Room()));

        assertThrows(IllegalArgumentException.class, () ->
                roomService.addRoom("301", 2, new BigDecimal("5000")));
    }

    @Test
    void testUpdateRoom_Success() {
        Room existing = new Room("301", 2, 1, new BigDecimal("5000"));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room updated = roomService.updateRoom(1L, "301-A", 3, new BigDecimal("5500"));

        assertEquals("301-A", updated.getRoomNumber());
        assertEquals(3, updated.getCapacity());
        assertEquals(new BigDecimal("5500"), updated.getRentPerPerson());
    }

    @Test
    void testAssignHostellerToRoom_Success() {
        User student = new User();
        student.setFullName("John Doe");
        student.setRole(com.hostel.hostel_management.entity.Role.HOSTELER);

        Room room = new Room("101", 2, 0, new BigDecimal("4000"));

        when(userRepository.findById(10L)).thenReturn(Optional.of(student));
        when(roomRepository.findById(5L)).thenReturn(Optional.of(room));
        when(userRepository.save(any(User.class))).thenReturn(student);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        User assigned = roomService.assignHostellerToRoom(10L, 5L);

        assertNotNull(assigned);
        assertEquals(1, room.getOccupied());
        assertEquals(room, student.getRoom());
    }

    @Test
    void testDeleteRoom_WithOccupants_UnassignsAndDeletes() {
        Room room = new Room("101", 4, 1, new BigDecimal("5000"));
        User student = new User();
        student.setRoom(room);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findByRoomId(1L)).thenReturn(java.util.List.of(student));

        roomService.deleteRoom(1L);

        org.junit.jupiter.api.Assertions.assertNull(student.getRoom());
        verify(userRepository).save(student);
        verify(roomRepository).delete(room);
    }
}
