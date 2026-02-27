package com.tamthong.trackr.core.importer;

import com.tamthong.trackr.category.Category;
import com.tamthong.trackr.category.CategoryRepository;
import com.tamthong.trackr.core.exception.ResourceNotFoundException;
import com.tamthong.trackr.core.importer.dto.ImportCategoryDto;
import com.tamthong.trackr.core.importer.dto.ImportDataDto;
import com.tamthong.trackr.core.importer.dto.ImportItemDto;
import com.tamthong.trackr.item.TrackedItem;
import com.tamthong.trackr.item.TrackedItemRepository;
import com.tamthong.trackr.user.User;
import com.tamthong.trackr.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final CategoryRepository categoryRepository;
    private final TrackedItemRepository trackedItemRepository;
    private final UserRepository userRepository;
    private final ImportMapper importMapper;

    @Transactional
    public void importData(ImportDataDto data, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Map<UUID, Category> resolvedCategories = processCategories(data.getCategories(), user);

        processItems(data.getItems(), user, resolvedCategories);
    }

    private Map<UUID, Category> processCategories(List<ImportCategoryDto> categoryDtos, User user) {
        List<UUID> categoryIds = categoryDtos.stream().map(ImportCategoryDto::getId).toList();
        Map<UUID, Category> existingCategories = categoryRepository.findAllById(categoryIds)
                .stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Map<UUID, Category> resolvedCategories = new HashMap<>();

        for (ImportCategoryDto dto : categoryDtos) {
            Category category = resolvedCategories.get(dto.getId());
            if (category == null) {
                category = existingCategories.get(dto.getId());
            }

            if (category != null) {
                Category mapped = importMapper.toCategory(dto, user);
                category.setName(mapped.getName());
                category.setType(mapped.getType());
                category.setDescription(mapped.getDescription());
                category.setDeleted(mapped.isDeleted());
            } else {
                category = importMapper.toCategory(dto, user);
                category.setId(null);
                category = categoryRepository.save(category);
            }
            resolvedCategories.put(dto.getId(), category);
        }

        return resolvedCategories;
    }

    private void processItems(List<ImportItemDto> itemDtos, User user, Map<UUID, Category> resolvedCategories) {
        List<UUID> itemIds = itemDtos.stream().map(ImportItemDto::getId).toList();
        Map<UUID, TrackedItem> existingItems = trackedItemRepository.findAllById(itemIds)
                .stream().collect(Collectors.toMap(TrackedItem::getId, Function.identity()));

        Map<UUID, TrackedItem> resolvedItems = new HashMap<>();

        for (ImportItemDto dto : itemDtos) {
            Category category = resolvedCategories.get(dto.getCategoryId());
            if (category == null) {
                throw new ResourceNotFoundException("Category not found for ID: " + dto.getCategoryId());
            }

            TrackedItem item = resolvedItems.get(dto.getId());
            if (item == null) {
                item = existingItems.get(dto.getId());
            }

            if (item != null) {
                TrackedItem mapped = importMapper.toTrackedItem(dto, user, category);
                item.setCategory(category);
                item.setTitle(mapped.getTitle());
                item.setSubtitle(mapped.getSubtitle());
                item.setStatus(mapped.getStatus());
                item.setCoverUrl(mapped.getCoverUrl());
                item.setDescription(mapped.getDescription());
                item.setDeleted(mapped.isDeleted());
                item.setMetadata(mapped.getMetadata());
            } else {
                item = importMapper.toTrackedItem(dto, user, category);
                item.setId(null);
                item = trackedItemRepository.save(item);
            }
            resolvedItems.put(dto.getId(), item);
        }
    }
}