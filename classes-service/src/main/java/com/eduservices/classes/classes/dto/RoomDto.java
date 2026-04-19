package com.eduservices.classes.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private Long id;
    private String name;
    private String block;
    private String description;
    private String roomNumber;
    private String capacity;
    private String type;
    private String location;
    private String availability;
    private String resources;
}
