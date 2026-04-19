package com.eduservices.profile.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetailsDto {

    private Long id;
    @NotEmpty(message = "{address.line1}")
    private String addressLine1;
    private String addressLine2;
    private String unitNumber;
    private String unitName;
    private String locality;
    @NotEmpty(message = "{address.city}")
    private String city;
    @NotEmpty(message = "{address.district}")
    private String district;
    @NotEmpty(message = "{address.state}")
    private String state;
    @NotEmpty(message = "{address.postal.code}")
    private String postalCode;
    @NotEmpty(message = "{address.country.code}")
    private String countryCode;
    @NotEmpty(message = "{address.country.name}")
    private String countryName;
    private String latitude;
    private String longitude;
    private String region;
    @NotEmpty(message = "{address.landmark}")
    private String landmark;
    @NotEmpty(message = "{address.type}")
    private String addressType;
    @NotEmpty(message = "{address.status}")
    private String status;
    Set<DocumentDto> addressDocuments;
}
