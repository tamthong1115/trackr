package com.tamthong.trackr.item.record;

import java.io.Serializable;

public record TrackingPlace(
        String id,
        String name,
        String address,
        String status,
        Double rating,
        String notes,
        String price,
        String foodType
) implements Serializable {}
