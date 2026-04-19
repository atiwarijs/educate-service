package com.eduservices.classes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import com.eduservices.common.service.Encrypt;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.Set;  


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "timetables", schema = "schools")
public class TimetableEntity extends BaseEntity {

 

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{period.name}")
    @Column(name = "period_name", nullable = false, length = 50)
    private String periodName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{days}")
    @Column(name = "days")
    private String day; 

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{start.time}")
    @Column(name = "start_time")
    private String startTime;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{end.time}")
    @Column(name = "end_time")
    private String endTime;

    @ManyToMany
    @JoinTable(
        name = "timetable_class_mapping",
        schema = "schools",
        joinColumns = @JoinColumn(name = "timetable_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<ClassEntity> classes;

    @ManyToMany
    @JoinTable(
        name = "timetable_section_mapping",
        schema = "schools",
        joinColumns = @JoinColumn(name = "timetable_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<SectionEntity> sections;

    @ManyToMany
    @JoinTable(
        name = "timetable_subject_mapping",
        schema = "schools",
        joinColumns = @JoinColumn(name = "timetable_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<SubjectEntity> subjects;


}
