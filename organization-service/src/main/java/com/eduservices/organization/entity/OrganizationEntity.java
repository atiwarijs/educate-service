package com.eduservices.organization.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eduservices.organization.service.Encrypt;

@Getter
@Setter
@Entity
@Table(name = "organizations_details", schema = "organizations")
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity extends BaseEntity {

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.name}")
    @Column(name = "org_name", nullable = false)
    private String orgName;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.code}")
    @Column(name = "org_code", nullable = false)
    private String orgCode;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.title}")
    @Column(name = "org_title", nullable = false)
    private String orgTitle;
    @Convert(converter = Encrypt.class)
    @Column(name = "org_sub_title", nullable = false)
    private String orgSubTitle;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.location}")
    @Column(name = "org_location", nullable = false)
    private String orgLocation;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.website}")
    @Column(name = "org_website_link", nullable = false)
    private String orgWebsiteLink;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.email}")
    @Column(name = "org_email", nullable = false)
    private String orgEmailId;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.fax}")
    @Column(name = "org_fax", nullable = false)
    private String orgFaxNo;
    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{org.phone}")
    @Column(name = "org_phone", nullable = false)
    private String orgPhoneNo;
    @Convert(converter = Encrypt.class)
    @Column(name = "org_type", nullable = false)
    private String orgType;
    @Convert(converter = Encrypt.class)
    @Column(name = "org_status", nullable = false)
    private String status;
    @Column(name = "parent_org_id", nullable = true)
    private Long parentOrgId;
}
