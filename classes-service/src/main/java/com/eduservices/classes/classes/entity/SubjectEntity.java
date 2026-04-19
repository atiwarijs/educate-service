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
@Table(name = "subjects", schema = "schools")
public class SubjectEntity extends BaseEntity {

    @NotEmpty(message = "{subject.name}")
    @Column(name = "subject_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "subject_code", unique = true, length = 20)
    @NotEmpty(message = "{subject.code}")
    private String code;

    @Column(name = "subject_description")
    @NotEmpty(message = "{subject.description}")
    private String description;

    @ManyToMany(mappedBy = "subjects")
    private List<ClassEntity> classes;
}

