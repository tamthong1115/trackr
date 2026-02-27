package com.tamthong.trackr.item.record;

import java.io.Serializable;

public record CommunityRating(
        String source,
        Double score
) implements Serializable {}