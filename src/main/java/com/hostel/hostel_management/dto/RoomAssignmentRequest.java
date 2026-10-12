
package com.hostel.hostel_management.dto;

public class RoomAssignmentRequest {

    private Long hostellerId;
    private Long roomId;

    public RoomAssignmentRequest() {
    }

    public Long getHostellerId() {
        return hostellerId;
    }

    public void setHostellerId(Long hostellerId) {
        this.hostellerId = hostellerId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
