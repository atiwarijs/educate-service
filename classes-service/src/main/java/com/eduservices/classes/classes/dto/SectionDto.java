package com.eduservices.classes.dto;

import com.eduservices.classes.entity.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDto extends BaseEntity {

    private Long id;

    @NotEmpty(message = "{section.name}")
    private String name;

    @NotEmpty(message = "{section.code}")
    private String code;

    @NotEmpty(message = "{section.description}")
    private String description;
}
