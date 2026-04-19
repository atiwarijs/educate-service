package com.eduservices.profiles.entity;

import java.util.Set;

import com.eduservices.profiles.util.Encrypt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "personal_details")
public class PersonalDetails extends BaseEntity {

	@Column(name = "uuid", nullable = false, unique = true)
    private String userId;
	
    @NotEmpty(message = "{personal.firstName.NotEmpty}")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = true)
    private String middleName;

//    @NotEmpty(message = "{personal.lastName.NotEmpty}")
    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Convert(converter = Encrypt.class)
//    @NotEmpty(message = "{personal.gander.NotEmpty}")
    @Column(name = "gender", nullable = true)
    private String gender;

    @Convert(converter = Encrypt.class)
//    @NotEmpty(message = "{personal.caste.NotEmpty}")
    @Column(name = "caste", nullable = true)
    private String caste;

    @Convert(converter = Encrypt.class)
//    @NotEmpty(message = "{personal.community.NotEmpty}")
    @Column(name = "community", nullable = true)
    private String community;

    @Convert(converter = Encrypt.class)
//    @NotEmpty(message = "{personal.dob.NotEmpty}")
    @Column(name = "dob", nullable = true)
    private String dob;

    @NotEmpty(message = "{personal.email.NotEmpty}")
    @Convert(converter = Encrypt.class)
    @Column(name = "email", nullable = false)
    private String email;

//    @NotEmpty(message = "{personal.mobile.NotEmpty}")
    @Convert(converter = Encrypt.class)
    @Column(name = "mobile", nullable = true)
    private String mobile;

    @Convert(converter = Encrypt.class)
    @Column(name = "alternate_mobile", nullable = true)
    private String alternateMobile;

    @Convert(converter = Encrypt.class)
    @Column(name = "alternate_email", nullable = true)
    private String alternateEmail;

    @Convert(converter = Encrypt.class)
//    @NotEmpty(message = "{personal.maritalStatus.NotEmpty}")
    @Column(name = "marital_status", nullable = true)
    private String maritalStatus;
    
    @Column(name = "profile_status", nullable = true)
    private String profileStatus;
    
    @Column(name = "status")
    private boolean status;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_document",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> personalDocuments;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE,})
    @JoinTable(name = "mapping_person_family",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "family_id"))
    Set<FamilyMember> families;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_address",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    Set<AddressDetails> addresses;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_education",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "education_id"))
    Set<EducationDetails> educations;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_experience",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "experience_id"))
    Set<ExperienceDetails> experiences;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_vehicle",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    Set<VehicleDetails> vehicles;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_person_medical",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_id"))
    Set<MedicalDetails> medicals;
}
