package com.tamthong.trackr.item.record;

import com.tamthong.trackr.item.ItemMetadata;

import java.util.List;

public record MusicMetadata(
        List<String> genres
) implements ItemMetadata {}