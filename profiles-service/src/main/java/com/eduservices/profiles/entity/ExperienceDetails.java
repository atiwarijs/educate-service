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
@Table(name = "experience_details")
public class ExperienceDetails extends BaseEntity {


    @NotEmpty(message = "{experience.institutionName.NotEmpty}")
    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.designation.NotEmpty}")
    @Column(name = "designation", nullable = false)
    private String designation;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.skills.NotEmpty}")
    @Column(name = "skills", nullable = false, columnDefinition = "TEXT")
    private String skills;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.joiningDate.NotEmpty}")
    @Column(name = "joining_date", nullable = false)
    private String joiningDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.leavingDate.NotEmpty}")
    @Column(name = "leaving_date", nullable = false)
    private String leavingDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.leavingReason.NotEmpty}")
    @Column(name = "leaving_reason", nullable = false)
    private String leavingReason;

    @Convert(converter = Encrypt.class)
    @Column(name = "remark", nullable = true)
    private String remark;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{experience.status.NotEmpty}")
    @Column(name = "status", nullable = false)
    private String status;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_experience_documents",
            joinColumns = @JoinColumn(name = "experience_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> experienceDocuments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "experiences")
    @JsonIgnore
    private Set<PersonalDetails> personals = new HashSet<>();
}
