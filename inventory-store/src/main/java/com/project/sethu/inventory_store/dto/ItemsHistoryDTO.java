package com.project.sethu.inventory_store.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


@Data
public class ItemsHistoryDTO {

    private UUID id;
    private UUID itemId;
    private UUID organisationId;
    private String operation;

    // ── Name ─────────────────────────────────────────────
    private String oldName;
    private String newName;

    // ── Rate ─────────────────────────────────────────────
    private BigDecimal oldRate;
    private BigDecimal newRate;

    // ── Currency ─────────────────────────────────────────
    private String oldCurrency;
    private String newCurrency;

    // ── Status ───────────────────────────────────────────
    private Boolean oldIsActive;
    private Boolean newIsActive;

    // ── Audit ─────────────────────────────────────────────
    private OffsetDateTime changedAt;
    private String createdBy;
    private String updatedBy;
}