package com.tamthong.trackr.core.importer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportCategoryDto {
    private UUID id = null;
    private String name;
    private String type;
    private String description;
    private boolean deleted;
}
