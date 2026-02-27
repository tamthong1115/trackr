package com.tamthong.trackr.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamthong.trackr.item.ItemMetadata;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SyncItemDto(
        @JsonProperty("id") UUID trackingItemId,
        UUID categoryId,
        String title,
        String subtitle,
        String status,
        String coverUrl,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        boolean deleted,

        ItemMetadata metadata
) {}