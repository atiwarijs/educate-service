package com.eduservices.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassInfoDTO {
    private Long id;
    private String className;
    private String section;
    private String subject;
    private String description;
}
