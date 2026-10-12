package com.hostel.hostel_management.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoomRequest {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @NotNull(message = "Rent is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rent cannot be negative")
    private BigDecimal rentPerPerson;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getRentPerPerson() {
        return rentPerPerson;
    }

    public void setRentPerPerson(BigDecimal rentPerPerson) {
        this.rentPerPerson = rentPerPerson;
    }
}
