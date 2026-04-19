package com.eduservices.address.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address_details")
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetails extends BaseEntity {

    @NotEmpty(message = "{address.line1}")
    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2", nullable = true)
    private String addressLine2;

    @Column(name = "unit_number", nullable = true)
    private String unitNumber;
    @Column(name = "unit_name", nullable = true)
    private String unitName;

    @Column(name = "locality", nullable = true)
    private String locality;

    @NotEmpty(message = "{address.city}")
    @Column(name = "city", nullable = false)
    private String city;

    
    @NotEmpty(message = "{address.district}")
    @Column(name = "district", nullable = false)
    private String district;

    
    @NotEmpty(message = "{address.state}")
    @Column(name = "state", nullable = false)
    private String state;

    
    @NotEmpty(message = "{address.postal.code}")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    
    @NotEmpty(message = "{address.country.code}")
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    
    @NotEmpty(message = "{address.country.name}")
    @Column(name = "country_name", nullable = false)
    private String countryName;

    
    @Column(name = "latitude", nullable = true)
    private String latitude;

    
    @Column(name = "longitude", nullable = true)
    private String longitude;

    
    @Column(name = "region", nullable = true)
    private String region;

    @NotEmpty(message = "{address.landmark}")
    @Column(name = "landmark", nullable = false)
    private String landmark;

    
    @NotEmpty(message = "{address.type}")
    @Column(name = "address_type", nullable = false)
    private String addressType;

    
    @NotEmpty(message = "{address.status}")
    @Column(name = "status", nullable = false)
    private String status;

}
