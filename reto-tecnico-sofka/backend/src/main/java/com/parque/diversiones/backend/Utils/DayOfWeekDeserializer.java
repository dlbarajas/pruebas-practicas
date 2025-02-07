package com.parque.diversiones.backend.Utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.DayOfWeek;

public class DayOfWeekDeserializer extends JsonDeserializer<DayOfWeek> {
    @Override
    public DayOfWeek deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return DayOfWeek.valueOf(p.getValueAsString().toUpperCase());
    }
}
