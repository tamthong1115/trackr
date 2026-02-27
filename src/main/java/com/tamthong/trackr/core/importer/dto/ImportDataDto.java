package com.tamthong.trackr.core.importer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportDataDto {
    private int version;
    private String exportedAt;
    private List<ImportCategoryDto> categories;
    private List<ImportItemDto> items;

}
