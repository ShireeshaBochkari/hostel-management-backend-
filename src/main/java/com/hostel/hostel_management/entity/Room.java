
package com.hostel.hostel_management.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "rooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int occupied;

    @Column(name = "rent_per_person", nullable = false)
    private BigDecimal rentPerPerson;

    @Column(nullable = false)
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<User> occupants = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @jakarta.persistence.JoinTable(
        name = "room_amenities",
        joinColumns = @jakarta.persistence.JoinColumn(name = "room_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "amenity_id")
    )
    private java.util.Set<Amenity> amenities = new java.util.HashSet<>();

    // Required by JPA
    public Room() {
    }

    public Room(
        String roomNumber,
        int capacity,
        int occupied,
        BigDecimal rentPerPerson) {

    this.roomNumber = roomNumber;
    this.capacity = capacity;
    this.occupied = occupied;
    this.rentPerPerson = rentPerPerson;

    if (capacity > 0 && occupied >= capacity) {
        this.status = "FULL";
    } else {
        this.status = "AVAILABLE";
    }
}

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        if (capacity > 0 && occupied >= capacity) {
            status = "FULL";
        } else {
            status = "AVAILABLE";
        }
    }

    @Transient
    public int getAvailableBeds() {
        return capacity - occupied;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

   

    public int getOccupied() {
        return occupied;
    }

    
public void setCapacity(int capacity) {
    if (capacity < 1) {
        throw new IllegalArgumentException("Room capacity must be at least 1.");
    }
    if (capacity < this.occupied) {
        throw new IllegalArgumentException(
            "Capacity cannot be less than occupied beds."
        );
    }
    this.capacity = capacity;
}

public void setOccupied(int occupied) {
    if (occupied < 0 || occupied > this.capacity) {
        throw new IllegalArgumentException(
            "Occupied beds must be between 0 and room capacity."
        );
    }
    this.occupied = occupied;
}


    public BigDecimal getRentPerPerson() {
        return rentPerPerson;
    }

    public void setRentPerPerson(BigDecimal rentPerPerson) {
        this.rentPerPerson = rentPerPerson;
    }

    public String getStatus() {
        return status;
    }

    public List<User> getOccupants() {
        return occupants;
    }

    public void setOccupants(List<User> occupants) {
        this.occupants = occupants;
    }

    public java.util.Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(java.util.Set<Amenity> amenities) {
        this.amenities = amenities;
    }
}
