package com.tamthong.trackr.core.importer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tamthong.trackr.core.config.CustomOffsetDateTimeDeserializer;
import com.tamthong.trackr.item.ItemMetadata;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportItemDto {
    private UUID id = null;
    private UUID categoryId;
    private String title;
    private String subtitle;
    private String status;
    private Double score;
    private String coverUrl;
    private String description;
    private boolean deleted;

    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime createdAt;

    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime updatedAt;

    // Jackson will automatically look at the "type" inside the JSON
    // and instantiate the correct Record (e.g., TvShowMetadata)
    private ItemMetadata metadata;
}