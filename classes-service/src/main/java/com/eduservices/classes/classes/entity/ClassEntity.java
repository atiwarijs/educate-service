package com.eduservices.classes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "classes", schema = "schools")
public class ClassEntity extends BaseEntity {

    @NotEmpty(message = "{class.name}")
    @Column(name = "class_name", unique = true, length = 100)
    private String name;

    @Column(name = "class_code",unique = true, length = 20)
    @NotEmpty(message = "{class.code}")
    private String code;

    @Column(name = "class_description")
    @NotEmpty(message = "{class.description}")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "class_section_mapping",
        schema = "schools",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<SectionEntity> sections;

    @ManyToMany
    @JoinTable(
        name = "class_subject_mapping",
        schema = "schools",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectEntity> subjects;
}
