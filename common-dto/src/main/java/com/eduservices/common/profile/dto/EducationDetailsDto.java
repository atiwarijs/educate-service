package com.eduservices.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EducationDetailsDto {

    private Long id;
    @NotEmpty(message = "{education.courseName.NotEmpty}")
    private String courseName;
    @NotEmpty(message = "{education.courseSubject.NotEmpty}")
    private String courseSubject;
    @NotEmpty(message = "{education.collegeName.NotEmpty}")
    private String collegeName;
    @NotEmpty(message = "{education.collegeCity.NotEmpty}")
    private String collegeCity;
    @NotEmpty(message = "{education.collegeState.NotEmpty}")
    private String collegeState;
    private String universityName;
    @NotEmpty(message = "{education.boardName.NotEmpty}")
    private String boardName;
    @NotEmpty(message = "{education.startDate.NotEmpty}")
    private String startDate;
    @NotEmpty(message = "{education.endDate.NotEmpty}")
    private String endDate;
    @NotEmpty(message = "{education.obtainMarks.NotEmpty}")
    private String obtainMarks;
    @NotEmpty(message = "{education.maxMarks.NotEmpty}")
    private String maxMarks;
    @NotEmpty(message = "{education.status.NotEmpty}")
    private String status;
    private Set<DocumentDto> educationDocuments;
}
