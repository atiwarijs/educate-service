package com.eduservices.classes.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.eduservices.classes.entity.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*; 

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto extends BaseEntity {

    private Long id;

    @NotEmpty(message = "{class.name}")
    private String name;

    @NotEmpty(message = "{class.code}")
    private String code;

    @NotEmpty(message = "{class.description}")
    private String description;

    private Set<SectionDto> sections;

    private Set<SubjectDto> subjects;
}
