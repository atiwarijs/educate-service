package com.eduservices.registrations.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private Long id;
    private String regNumber;
    private String fees;
    private String feesPaid;
    private String feesPending;
    private String feesStatus;
    private String regType;
    private String regStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime enrollmentDate;
    private String remark;
    private Long profileId;
    private Long orgId;

}
