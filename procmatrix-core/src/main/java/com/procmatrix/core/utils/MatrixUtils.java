package com.procmatrix.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatrixUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertMatrixToJson(int[][] matrix) throws JsonProcessingException {
        return objectMapper.writeValueAsString(matrix);
    }

    public static int[][] convertJsonToMatrix(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, int[][].class);
    }
}