package com.qodana.matrixrotation.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatrixDeserializer extends StdDeserializer<int[][]> {

    public MatrixDeserializer() {
        super(int[][].class);
    }

    @Override
    public int[][] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<List<Integer>> tempList = new ArrayList<>();
        JsonNode node = p.getCodec().readTree(p);
        for (JsonNode rowNode : node) {
            List<Integer> row = new ArrayList<>();
            for (JsonNode elementNode : rowNode) {
                row.add(elementNode.asInt());
            }
            tempList.add(row);
        }
        int[][] matrix = new int[tempList.size()][];
        for (int i = 0; i < tempList.size(); i++) {
            List<Integer> row = tempList.get(i);
            matrix[i] = row.stream().mapToInt(Integer::intValue).toArray();
        }
        return matrix;
    }
}
