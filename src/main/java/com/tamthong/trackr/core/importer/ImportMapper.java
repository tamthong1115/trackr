package com.tamthong.trackr.core.importer;

import com.tamthong.trackr.category.Category;
import com.tamthong.trackr.core.importer.dto.ImportCategoryDto;
import com.tamthong.trackr.core.importer.dto.ImportItemDto;
import com.tamthong.trackr.item.TrackedItem;
import com.tamthong.trackr.user.User;
import org.springframework.stereotype.Component;

@Component
public class ImportMapper {

    public Category toCategory(ImportCategoryDto dto, User user){
        if(dto == null) {
            return null;
        }

        return Category.builder()
                .id(dto.getId())
                .user(user)
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .deleted(dto.isDeleted())
                .build();
    }

    public TrackedItem toTrackedItem(ImportItemDto dto, User user, Category category) {
        if (dto == null) {
            return null;
        }
        return TrackedItem.builder()
                .id(dto.getId())
                .user(user)
                .category(category)
                .title(dto.getTitle())
                .subtitle(dto.getSubtitle())
                .status(dto.getStatus())
                .coverUrl(dto.getCoverUrl())
                .description(dto.getDescription())
                .deleted(dto.isDeleted())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .metadata(dto.getMetadata())
                .build();
    }
}