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
@Table(name = "family_members")
public class FamilyMember extends BaseEntity {

    @NotEmpty(message = "{family.memberName.NotEmpty}")
    @Column(name = "member_name",nullable = false)
    private String memberName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{family.memberRelation.NotEmpty}")
    @Column(name = "member_relation",nullable = false)
    private String memberRelation;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{family.memberGender.NotEmpty}")
    @Column(name = "member_gender",nullable = false)
    private String memberGender;

    @NotEmpty(message = "{family.memberDob.NotEmpty}")
    @Convert(converter = Encrypt.class)
    @Column(name = "member_dob",nullable = false)
    private String memberDob;

    @Convert(converter = Encrypt.class)
    @Column(name = "member_occupation",nullable = true)
    private String memberOccupation;

    @Convert(converter = Encrypt.class)
    @Column(name = "member_occupation_org",nullable = true)
    private String memberOrg;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{family.memberStatus.status}")
    @Column(name = "member_status",nullable = false)
    private String memberStatus;

    @Convert(converter = Encrypt.class)
    @Column(name = "member_email",nullable = true)
    private String memberEmail;

    @Convert(converter = Encrypt.class)
    @Column(name = "member_mobile",nullable = true)
    private String memberMobile;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_member_documents",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> memberDocuments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "families")
    @JsonIgnore
    private Set<PersonalDetails> personals = new HashSet<>();
}
