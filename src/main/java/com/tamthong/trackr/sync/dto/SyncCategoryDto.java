package com.tamthong.trackr.sync.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SyncCategoryDto(
        UUID categoryId,
        String name,
        String type,
        String description,
        boolean deleted,
        OffsetDateTime updatedAt
) {}
