package com.tamthong.trackr.sync.dto;

import java.util.List;

public record SyncPushRequest(
        List<SyncCategoryDto> categories,
        List<SyncItemDto> items,
        List<SyncDeletedRecordDto> deletedRecords
) {}
