package com.hostel.hostel_management.dto;

import java.math.BigDecimal;

import com.hostel.hostel_management.entity.Room;

public class RoomResponse {

    private Long id;
    private String roomNumber;
    private int capacity;
    private int occupied;
    private int availableBeds;
    private BigDecimal rentPerPerson;
    private String status;
    private java.util.List<String> amenities;

    public static RoomResponse from(Room room) {
        RoomResponse response = new RoomResponse();
        response.id = room.getId();
        response.roomNumber = room.getRoomNumber();
        response.capacity = room.getCapacity();
        response.occupied = room.getOccupied();
        response.availableBeds = room.getAvailableBeds();
        response.rentPerPerson = room.getRentPerPerson();
        response.status = room.getStatus();
        if (room.getAmenities() != null) {
            response.amenities = room.getAmenities().stream()
                    .map(com.hostel.hostel_management.entity.Amenity::getName)
                    .toList();
        } else {
            response.amenities = java.util.Collections.emptyList();
        }
        return response;
    }

    public java.util.List<String> getAmenities() {
        return amenities;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public BigDecimal getRentPerPerson() {
        return rentPerPerson;
    }

    public String getStatus() {
        return status;
    }
}
