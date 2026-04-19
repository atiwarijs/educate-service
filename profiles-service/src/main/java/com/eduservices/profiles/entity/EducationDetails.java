package com.eduservices.profiles.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import com.eduservices.profiles.util.Encrypt;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "education_details")
public class EducationDetails extends BaseEntity {

    @NotEmpty(message = "{education.courseName.NotEmpty}")
    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.courseSubject.NotEmpty}")
    @Column(name = "course_subject", nullable = false)
    private String courseSubject;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.collegeName.NotEmpty}")
    @Column(name = "college_name", nullable = false)
    private String collegeName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.collegeCity.NotEmpty}")
    @Column(name = "college_city", nullable = false)
    private String collegeCity;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.collegeState.NotEmpty}")
    @Column(name = "college_state", nullable = false)
    private String collegeState;

    @Convert(converter = Encrypt.class)
    @Column(name = "university_name", nullable = false)
    private String universityName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.boardName.NotEmpty}")
    @Column(name = "board_name", nullable = false)
    private String boardName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.startDate.NotEmpty}")
    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.endDate.NotEmpty}")
    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.obtainMarks.NotEmpty}")
    @Column(name = "obtain_marks", nullable = false)
    private String obtainMarks;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.maxMarks.NotEmpty}")
    @Column(name = "max_marks", nullable = false)
    private String maxMarks;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{education.status.NotEmpty}")
    @Column(name = "status", nullable = false)
    private String status;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_education_documents",
            joinColumns = @JoinColumn(name = "education_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> educationDocuments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "educations")
    @JsonIgnore
    private Set<PersonalDetails> personals = new HashSet<>();
}
