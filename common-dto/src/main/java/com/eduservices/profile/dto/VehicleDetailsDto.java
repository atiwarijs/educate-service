package com.eduservices.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailsDto {

    private Long id;
    @NotEmpty(message = "{vehicle.registrationNumber.NotEmpty}")
    private String registrationNumber;
    @NotEmpty(message = "{vehicle.registrationDate.NotEmpty}")
    private String registrationDate;
    @NotEmpty(message = "{vehicle.chassisNumber.NotEmpty}")
    private String chassisNumber;
    @NotEmpty(message = "{vehicle.engineNumber.NotEmpty}")
    private String engineNumber;
    @NotEmpty(message = "{vehicle.brandName.NotEmpty}")
    private String brandName;
    @NotEmpty(message = "{vehicle.vehicleType.NotEmpty}")
    private String vehicleType;
    @NotEmpty(message = "{vehicle.fuelType.NotEmpty}")
    private String fuelType;
    @NotEmpty(message = "{vehicle.dlNumber.NotEmpty}")
    private String dlNumber;
    @NotEmpty(message = "{vehicle.dlExpireDate.NotEmpty}")
    private String dlExpireDate;
    @NotEmpty(message = "{vehicle.isInsured.NotEmpty}")
    private String isInsured;
    @NotEmpty(message = "{vehicle.insuranceId.NotEmpty}")
    private String insuranceId;
    @NotEmpty(message = "{vehicle.insuranceCompanyName.NotEmpty}")
    private String insuranceCompanyName;
    @NotEmpty(message = "{vehicle.insuranceType.NotEmpty}")
    private String insuranceType;
    @NotEmpty(message = "{vehicle.insuranceExpireDate.NotEmpty}")
    private String insuranceExpireDate;
    @NotEmpty(message = "{vehicle.isPucCertificated.NotEmpty}")
    private String isPucCertificated;
    @NotEmpty(message = "{vehicle.pucCertificateId.NotEmpty}")
    private String pucCertificateId;
    @NotEmpty(message = "{vehicle.pucExpireDate.NotEmpty}")
    private String pucExpireDate;
    private String remark;
    private Set<DocumentDto> vehicleDocuments;

}
