package com.eduservices.classes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms_assign", schema = "schools")
public class RoomAssignEntity extends BaseEntity {

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section_id")
    private Long sectionId;



    @Column(name = "status")
    @NotEmpty(message = "{room.assign.status}")
    private String status;

}
