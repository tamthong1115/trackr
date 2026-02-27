package com.tamthong.trackr.item.record;

import com.tamthong.trackr.item.ItemMetadata;

import java.util.List;

public record BookMetadata(
        String googleBookId,
        List<String> genres
) implements ItemMetadata {}