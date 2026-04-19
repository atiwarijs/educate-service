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
@Table(name = "sections", schema = "schools")
public class SectionEntity extends BaseEntity {

    @NotEmpty(message = "{section.name}")
    @Column(name = "section_name", unique = true, length = 100)
    private String name;

    @Column(name = "section_code", unique = true, length = 20)
    @NotEmpty(message = "{section.code}")
    private String code;

    @Column(name = "section_description")
    @NotEmpty(message = "{section.description}")
    private String description;

    @ManyToMany(mappedBy = "sections")
    private List<ClassEntity> classes;
}

