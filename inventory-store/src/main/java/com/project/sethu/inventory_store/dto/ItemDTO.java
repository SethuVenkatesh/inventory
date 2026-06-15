package com.project.sethu.inventory_store.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ItemDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @NotBlank(groups = {ItemDTO.OnCreate.class}, message = "Item Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    @NotNull(groups = ItemDTO.OnCreate.class, message = "Rate is required")
    @DecimalMin(groups = {ItemDTO.OnCreate.class,ItemDTO.OnUpdate.class}, value = "0.0", message = "Rate must be 0 or greater")
    @Digits(groups = {ItemDTO.OnCreate.class,ItemDTO.OnUpdate.class},integer = 14, fraction = 4, message = "Rate must have up to 14 integer digits and 4 decimal places")
    private BigDecimal rate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String currency = "INR";
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255, message = "Created by must not exceed 255 characters")
    private String createdBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255, message = "Updated by must not exceed 255 characters")
    private String updatedBy;
    public interface OnCreate {}
    public interface OnUpdate {}
}