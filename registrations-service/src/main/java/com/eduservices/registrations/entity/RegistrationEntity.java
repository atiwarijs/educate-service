package com.eduservices.registrations.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import com.eduservices.common.service.Encrypt;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "new_registrations_details", schema = "registrations")
public class RegistrationEntity extends BaseEntity {

    @Column(name = "registration_number",nullable = false, unique = true, length = 20, updatable = false)
    private String regNumber;
    @Convert(converter = Encrypt.class)
    @Column(name = "registration_fees")
    @NotEmpty(message = "{registration.fees}")
    private String fees;
    @Convert(converter = Encrypt.class)
    @Column(name = "registration_fees_paid")
    private String feesPaid;
    @Convert(converter = Encrypt.class)
    @Column(name = "registration_fees_pending")
    private String feesPending;
    @Column(name = "registration_fees_status", length = 20)
    @NotEmpty(message = "{registration.fees.status}")
    private String feesStatus;
    @Column(name = "registration_type", length = 20)
    @NotEmpty(message = "{registration.type}")
    private String regType;
    @Column(name = "registration_status", length = 20)
    @NotEmpty(message = "{registration.status}")
    private String regStatus;
    @CreationTimestamp
    @Column(name = "date_of_registration")
    private LocalDateTime registrationDate;
    @Column(name = "date_of_enrollment", nullable = true)
    private LocalDateTime enrollmentDate;
    private String remark;
    @Column(nullable = false, unique = true)
    private Long profileId;

}
