package com.tamthong.trackr.sync.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SyncDeletedRecordDto(
        UUID recordId,
        String tableName,
        OffsetDateTime deletedAt
) {}
