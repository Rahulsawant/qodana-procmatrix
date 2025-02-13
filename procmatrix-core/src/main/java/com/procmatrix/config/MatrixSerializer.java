package com.procmatrix.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MatrixSerializer extends JsonSerializer<int[][]> {
    @Override
    public void serialize(int[][] matrix, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (int[] row : matrix) {
            gen.writeStartArray();
            for (int value : row) {
                gen.writeNumber(value);
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();
    }
}
