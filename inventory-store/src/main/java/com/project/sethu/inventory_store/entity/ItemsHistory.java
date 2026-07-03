package com.project.sethu.inventory_store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "items_history")
@Getter
@Setter
public class ItemsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "organisation_id", nullable = false)
    private UUID organisationId;

    @Column(name = "operation", nullable = false)
    private String operation;       // INSERT / UPDATE / DELETE

    // ── Name ─────────────────────────────────────────────
    @Column(name = "old_name")
    private String oldName;

    @Column(name = "new_name")
    private String newName;

    // ── Rate ─────────────────────────────────────────────
    @Column(name = "old_rate", precision = 18, scale = 4)
    private BigDecimal oldRate;

    @Column(name = "new_rate", precision = 18, scale = 4)
    private BigDecimal newRate;

    // ── Currency ─────────────────────────────────────────
    @Column(name = "old_currency", length = 3)
    private String oldCurrency;

    @Column(name = "new_currency", length = 3)
    private String newCurrency;

    // ── Status ───────────────────────────────────────────
    @Column(name = "old_is_active")
    private Boolean oldIsActive;

    @Column(name = "new_is_active")
    private Boolean newIsActive;

    // ── Audit ─────────────────────────────────────────────
    @Column(name = "changed_at", nullable = false, updatable = false)
    private OffsetDateTime changedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
}