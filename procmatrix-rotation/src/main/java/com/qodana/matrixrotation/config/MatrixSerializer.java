package com.qodana.matrixrotation.config;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MatrixSerializer extends StdSerializer<int[][]> {

    public MatrixSerializer() {
        super(int[][].class);
    }

    @Override
    public void serialize(int[][] matrix, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
