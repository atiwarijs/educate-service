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
@Table(name = "vehicle_details")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetails extends BaseEntity {

    @NotEmpty(message = "{vehicle.registrationNumber.NotEmpty}")
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.registrationDate.NotEmpty}")
    @Column(name = "registration_date", nullable = false)
    private String registrationDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.chassisNumber.NotEmpty}")
    @Column(name = "chassis_number", nullable = false)
    private String chassisNumber;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.engineNumber.NotEmpty}")
    @Column(name = "engine_number", nullable = false)
    private String engineNumber;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.brandName.NotEmpty}")
    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.vehicleType.NotEmpty}")
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.fuelType.NotEmpty}")
    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.dlNumber.NotEmpty}")
    @Column(name = "dl_number", nullable = false)
    private String dlNumber;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.dlExpireDate.NotEmpty}")
    @Column(name = "dl_expire_date", nullable = false)
    private String dlExpireDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.isInsured.NotEmpty}")
    @Column(name = "is_insured", nullable = false)
    private String isInsured;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.insuranceId.NotEmpty}")
    @Column(name = "insurance_id", nullable = false)
    private String insuranceId;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.insuranceCompanyName.NotEmpty}")
    @Column(name = "insurance_company_name", nullable = false)
    private String insuranceCompanyName;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.insuranceType.NotEmpty}")
    @Column(name = "insurance_type", nullable = false)
    private String insuranceType;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.insuranceExpireDate.NotEmpty}")
    @Column(name = "insurance_expire_date", nullable = false)
    private String insuranceExpireDate;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.isPucCertificated.NotEmpty}")
    @Column(name = "is_puc_certificated", nullable = false)
    private String isPucCertificated;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.pucCertificateId.NotEmpty}")
    @Column(name = "puc_certificate_id", nullable = false)
    private String pucCertificateId;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{vehicle.pucExpireDate.NotEmpty}")
    @Column(name = "puc_expire_date", nullable = false)
    private String pucExpireDate;

    @Convert(converter = Encrypt.class)
    @Column(name = "remark", nullable = true)
    private String remark;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "member_profile_doc_id", nullable = true)
    private Document memberProfileImage;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_vehicle_documents",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> vehicleDocuments;


    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "vehicles")
    @JsonIgnore
    private Set<PersonalDetails> persons = new HashSet<>();
}
