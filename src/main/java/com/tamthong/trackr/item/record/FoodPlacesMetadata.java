package com.tamthong.trackr.item.record;

import com.tamthong.trackr.item.ItemMetadata;

import java.util.List;

public record FoodPlacesMetadata(
        String subtype,
        String address,
        String price,
        List<TrackingPlace> places
) implements ItemMetadata {}
