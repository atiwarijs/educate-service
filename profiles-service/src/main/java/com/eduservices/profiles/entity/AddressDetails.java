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
@Table(name = "address_details")
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetails extends BaseEntity {

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.line1}")
    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Convert(converter = Encrypt.class)
    @Column(name = "address_line2", nullable = true)
    private String addressLine2;

    @Convert(converter = Encrypt.class)
    @Column(name = "unit_number", nullable = true)
    private String unitNumber;

    @Convert(converter = Encrypt.class)
    @Column(name = "unit_name", nullable = true)
    private String unitName;

    @Convert(converter = Encrypt.class)
    @Column(name = "locality", nullable = true)
    private String locality;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.city}")
    @Column(name = "city", nullable = false)
    private String city;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.district}")
    @Column(name = "district", nullable = false)
    private String district;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.state}")
    @Column(name = "state", nullable = false)
    private String state;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.postal.code}")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.country.code}")
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.country.name}")
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Convert(converter = Encrypt.class)
    @Column(name = "latitude", nullable = true)
    private String latitude;

    @Convert(converter = Encrypt.class)
    @Column(name = "longitude", nullable = true)
    private String longitude;

    @Convert(converter = Encrypt.class)
    @Column(name = "region", nullable = true)
    private String region;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.landmark}")
    @Column(name = "landmark", nullable = false)
    private String landmark;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.type}")
    @Column(name = "address_type", nullable = false)
    private String addressType;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{address.status}")
    @Column(name = "status", nullable = false)
    private String status;

    @Valid
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "mapping_address_documents",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    Set<Document> addressDocuments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "addresses")
    @JsonIgnore
    private Set<PersonalDetails> personals = new HashSet<>();
}
