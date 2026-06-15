package com.project.sethu.inventory_store.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String role;
    private UUID organisationId;

    public AuthResponse(String token, String name, String email,
                        String role, UUID organisationId) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.organisationId = organisationId;
    }

}
