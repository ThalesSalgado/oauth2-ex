package com.thales.oauth.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDateTime> {

    public static final String DATA_PATTERN = "dd-MM-yyyy HH:mm:ss";

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        if (value != null)
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DATA_PATTERN)));
        else
            gen.writeNull();
    }
}
