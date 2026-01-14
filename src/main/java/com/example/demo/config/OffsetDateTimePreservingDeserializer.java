package com.example.demo.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimePreservingDeserializer extends JsonDeserializer<OffsetDateTime> {
    @Override
    public OffsetDateTime deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext
    ) throws IOException, JacksonException {
        return OffsetDateTime.parse(jsonParser.getText());
    }
}
