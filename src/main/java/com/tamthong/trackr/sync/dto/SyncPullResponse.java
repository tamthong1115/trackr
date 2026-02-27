package com.tamthong.trackr.sync.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record SyncPullResponse(
        List<SyncCategoryDto> categories,
        List<SyncItemDto> items,
        List<SyncDeletedRecordDto> deletedRecords,
        OffsetDateTime serverTimestamp
) {}
