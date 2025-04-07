package ru.eriknas.brokenstore.controllers.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class StrictDoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (!node.isDouble() || node.isInt()) {
            throw new IllegalArgumentException("Поле должно быить типом Double");
        }
        return node.asDouble();
    }

}
