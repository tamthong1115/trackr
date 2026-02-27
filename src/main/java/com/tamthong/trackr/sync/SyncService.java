package com.tamthong.trackr.sync;

import com.tamthong.trackr.category.Category;
import com.tamthong.trackr.category.CategoryRepository;
import com.tamthong.trackr.core.exception.ResourceNotFoundException;
import com.tamthong.trackr.item.TrackedItem;
import com.tamthong.trackr.item.TrackedItemRepository;
import com.tamthong.trackr.item.record.*;
import com.tamthong.trackr.sync.dto.SyncCategoryDto;
import com.tamthong.trackr.sync.dto.SyncDeletedRecordDto;
import com.tamthong.trackr.sync.dto.SyncItemDto;
import com.tamthong.trackr.sync.dto.SyncPullResponse;
import com.tamthong.trackr.sync.dto.SyncPushRequest;
import com.tamthong.trackr.user.User;
import com.tamthong.trackr.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final CategoryRepository categoryRepository;
    private final TrackedItemRepository trackedItemRepository;
    private final DeletedRecordRepository deletedRecordRepository;
    private final UserRepository userRepository;

    @Transactional
    public void push(SyncPushRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.items() != null) {
            for (SyncItemDto dto : request.items()) {
                TrackedItem item = trackedItemRepository.findById(dto.trackingItemId())
                        .orElse(new TrackedItem());

                item.setId(dto.trackingItemId());
                item.setUser(user);

                if (dto.categoryId() != null) {
                    Category cat = categoryRepository.findById(dto.categoryId()).orElse(null);
                    item.setCategory(cat);
                }

                // Standard Fields
                item.setTitle(dto.title());
                item.setSubtitle(dto.subtitle());
                item.setStatus(dto.status());
                item.setCoverUrl(dto.coverUrl());
                item.setDescription(dto.description());
                item.setDeleted(dto.deleted());
                item.setCreatedAt(dto.createdAt());
                item.setUpdatedAt(OffsetDateTime.now());
                item.setMetadata(dto.metadata());

                trackedItemRepository.save(item);
            }
        }
    }


@Transactional(readOnly = true)
public SyncPullResponse pull(UUID userId, OffsetDateTime since) {
    List<Category> updatedCategories;
    List<TrackedItem> updatedItems;
    List<DeletedRecord> deletedRecords;

    if (since == null) {
        updatedCategories = categoryRepository.findByUserId(userId);
        updatedItems = trackedItemRepository.findByUserId(userId);
        deletedRecords = new ArrayList<>();
    } else {
        updatedCategories = categoryRepository.findByUserIdAndUpdatedAtAfter(userId, since);
        updatedItems = trackedItemRepository.findByUserIdAndUpdatedAtAfter(userId, since);
        deletedRecords = deletedRecordRepository.findByUserIdAndDeletedAtAfter(userId, since);
    }

    List<SyncCategoryDto> catDtos = updatedCategories.stream()
            .map(this::mapToCategoryDto)
            .collect(Collectors.toList());

    List<SyncItemDto> itemDtos = updatedItems.stream()
            .map(this::mapToItemDto)
            .collect(Collectors.toList());

    List<SyncDeletedRecordDto> delDtos = deletedRecords.stream()
            .map(r -> new SyncDeletedRecordDto(r.getRecordId(), r.getTableName(), r.getDeletedAt()))
            .collect(Collectors.toList());

    return new SyncPullResponse(catDtos, itemDtos, delDtos, OffsetDateTime.now());
}

@Transactional
public void hardReset(UUID userId) {
    deletedRecordRepository.deleteByUserId(userId);
    trackedItemRepository.deleteByUserId(userId);
    categoryRepository.deleteByUserId(userId);
}

private SyncCategoryDto mapToCategoryDto(Category cat) {
    return new SyncCategoryDto(
            cat.getId(),
            cat.getName(),
            cat.getType(),
            cat.getDescription(),
            cat.isDeleted(),
            cat.getUpdatedAt()
    );
}

private SyncItemDto mapToItemDto(TrackedItem item) {
    return new SyncItemDto(
            item.getId(),
            item.getCategory() != null ? item.getCategory().getId() : null,
            item.getTitle(),
            item.getSubtitle(),
            item.getStatus(),
            item.getCoverUrl(),
            item.getDescription(),
            item.getCreatedAt(),
            item.getUpdatedAt(),
            item.isDeleted(),
            item.getMetadata()
    );
}

}
