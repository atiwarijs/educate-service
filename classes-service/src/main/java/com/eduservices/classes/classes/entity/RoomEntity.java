package com.eduservices.classes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import jakarta.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms", schema = "schools")
public class RoomEntity extends BaseEntity {

    @Column(name = "room_number", unique = true, length = 10)
    @NotEmpty(message = "{room.number}")
    private String roomNumber;

    @NotEmpty(message = "{room.name}")
    @Column(name = "room_name", unique = true, length = 50)
    private String name;

    @NotEmpty(message = "{room.block}")
    @Column(name = "block_name", unique = false, length = 50)
    private String block;
 
    @Column(name = "room_description")
    @NotEmpty(message = "{room.description}")
    private String description;    

    @Column(name = "room_capacity")
    @NotEmpty(message = "{room.capacity}")
    private String capacity;

    @Column(name = "room_type")
    @NotEmpty(message = "{room.type}")
    private String type;

    @Column(name = "room_location")
    @NotEmpty(message = "{room.location}")
    private String location;

    @Column(name = "room_availability")
    @NotEmpty(message = "{room.availability}")
    private String availability;

    @Column(name = "room_resources") 
    private String resources;
    
}
