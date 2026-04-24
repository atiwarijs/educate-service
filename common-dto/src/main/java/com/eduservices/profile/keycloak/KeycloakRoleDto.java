package com.eduservices.profile.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Keycloak roles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakRoleDto {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
}