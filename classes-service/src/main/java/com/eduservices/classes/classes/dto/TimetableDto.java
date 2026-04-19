package com.eduservices.classes.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableDto {

    private Long id;

    @NotEmpty(message = "{class.name}")
    private String className;

    @NotEmpty(message = "{section.name}")
    private String sectionName;

    @NotEmpty(message = "{days}")
    private String day;

    @NotEmpty(message = "{subject.name}")
    private String subjectName;

    @NotEmpty(message = "{start.time}")
    private String startTime;

    @NotEmpty(message = "{end.time}")
    private String endTime;
}
