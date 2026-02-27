package com.tamthong.trackr.item.record;

import com.tamthong.trackr.item.ItemMetadata;

import java.util.List;

public record MangaMetadata(
        List<String> genres
) implements ItemMetadata {}