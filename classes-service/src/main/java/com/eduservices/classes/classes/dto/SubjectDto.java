package com.eduservices.classes.dto;

import com.eduservices.classes.entity.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto extends BaseEntity {

    private Long id;

    @NotEmpty(message = "{subject.name}")
    private String name;

    @NotEmpty(message = "{subject.code}")
    private String code;

    @NotEmpty(message = "{subject.description}")
    private String description;

}
