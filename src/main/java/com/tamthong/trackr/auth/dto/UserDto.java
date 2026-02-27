package com.tamthong.trackr.auth.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String role,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
