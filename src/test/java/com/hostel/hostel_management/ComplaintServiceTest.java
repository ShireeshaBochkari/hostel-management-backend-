package com.hostel.hostel_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hostel.hostel_management.entity.Complaint;
import com.hostel.hostel_management.entity.ComplaintStatus;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.ComplaintRepository;
import com.hostel.hostel_management.service.ComplaintService;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    private ComplaintService complaintService;

    @BeforeEach
    void setUp() {
        complaintService = new ComplaintService(complaintRepository);
    }

    @Test
    void testCreateComplaint_Success() {
        User student = new User();
        student.setFullName("Student User");
        student.setRole(com.hostel.hostel_management.entity.Role.HOSTELER);

        when(complaintRepository.save(any(Complaint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Complaint complaint = complaintService.create(student, "Leaking Pipe", "Bathroom tap in Room 101 is leaking");

        assertNotNull(complaint);
        assertEquals("Leaking Pipe", complaint.getTitle());
        assertEquals(ComplaintStatus.OPEN, complaint.getStatus());
        verify(complaintRepository).save(any(Complaint.class));
    }

    @Test
    void testUpdateStatus_Success() {
        Complaint complaint = new Complaint();
        complaint.setTitle("Fan not working");
        complaint.setStatus(ComplaintStatus.OPEN);

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(any(Complaint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Complaint updated = complaintService.updateStatus(1L, "RESOLVED", "Electrician replaced the capacitor.");

        assertEquals(ComplaintStatus.RESOLVED, updated.getStatus());
        assertEquals("Electrician replaced the capacitor.", updated.getStaffResponse());
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        Complaint complaint = new Complaint();
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        assertThrows(IllegalArgumentException.class, () ->
                complaintService.updateStatus(1L, "INVALID_STATUS", "Notes"));
    }
}
