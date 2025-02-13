package com.qodana.matrixrotation.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.hateoas.EntityModel;

import java.io.IOException;

public class EntityModelMatrixSerializer extends JsonSerializer<EntityModel<int[][]>> {

    @Override
    public void serialize(EntityModel<int[][]> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Start the object
        gen.writeStartObject();

        // Serialize the content (int[][] matrix)
        gen.writeFieldName("content");
        gen.writeStartArray();
        for (int[] row : value.getContent()) {
            gen.writeStartArray();
            for (int cell : row) {
                gen.writeNumber(cell);
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();

        // Serialize the links (if any)
        if (value.hasLinks()) {
            gen.writeFieldName("_links");
            gen.writeStartObject();
            value.getLinks().forEach(link -> {
                try {
                    gen.writeStringField(link.getRel().value(), link.getHref());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            gen.writeEndObject();
        }

        // End the object
        gen.writeEndObject();
    }
}

