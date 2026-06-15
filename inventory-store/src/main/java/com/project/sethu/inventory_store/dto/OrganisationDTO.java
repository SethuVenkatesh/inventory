package com.project.sethu.inventory_store.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class OrganisationDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @NotBlank(groups = {OnCreate.class,OnUpdate.class}, message = "Organisation Name is required")
    @Size(groups = {OnCreate.class,OnUpdate.class},min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    public interface OnCreate {}
    public interface OnUpdate {}

}