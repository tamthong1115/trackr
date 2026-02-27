package com.tamthong.trackr.core.config;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class CustomOffsetDateTimeDeserializer extends ValueDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
        String dateString = p.getText();
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            // First try standard parsing (if it has Z or offset)
            return OffsetDateTime.parse(dateString);
        } catch (DateTimeParseException e) {
            // If it fails, try parsing as LocalDateTime and assume UTC
            LocalDateTime localDateTime = LocalDateTime.parse(dateString);
            return localDateTime.atOffset(ZoneOffset.UTC);
        }
    }
}
