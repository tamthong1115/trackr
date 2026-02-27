package com.tamthong.trackr.item.record;

import com.tamthong.trackr.item.ItemMetadata;

import java.time.LocalDateTime;
import java.util.List;

public record GameMetadata(
        String igdbId,
        String studio,
        List<String> genres,
        LocalDateTime releaseDate,
        List<CommunityRating> communityRatings
) implements ItemMetadata {}