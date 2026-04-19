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
@Entity
@Table(name = "medical_details")
@NoArgsConstructor
@AllArgsConstructor
public class MedicalDetails extends BaseEntity {

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{medical.bloodGroup.NotEmpty}")
    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{medical.medicalHistory.NotEmpty}")
    @Column(name = "medical_history", nullable = true)
    private String medicalHistory;

    @Convert(converter = Encrypt.class)
    @Column(name = "diseases", nullable = true)
    private String diseases;

    @Convert(converter = Encrypt.class)
    @Column(name = "medications", nullable = true)
    private String medications;

    @Convert(converter = Encrypt.class)
    @Column(name = "symptoms", nullable = true)
    private String symptoms;

    @Convert(converter = Encrypt.class)
    @Column(name = "vitals", nullable = true)
    private String vitals;

    @Convert(converter = Encrypt.class)
    @Column(name = "diagnoses", nullable = true)
    private String diagnoses;

    @Convert(converter = Encrypt.class)
    @Column(name = "treatments", nullable = true)
    private String treatments;

    @Convert(converter = Encrypt.class)
    @Column(name = "therapies", nullable = true)
    private String therapies;

    @Convert(converter = Encrypt.class)
    @Column(name = "psychosocial", nullable = true)
    private String psychosocial;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_medical_documents",
            joinColumns = @JoinColumn(name = "medical_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> medicalDocuments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "medicals")
    @JsonIgnore
    private Set<PersonalDetails> persons = new HashSet<>();
}
