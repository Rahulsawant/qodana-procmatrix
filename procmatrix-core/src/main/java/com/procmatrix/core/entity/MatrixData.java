package com.procmatrix.core.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

public class MatrixData {

    private Long id;
    @Lob
    private String data;
    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int[][] getMatrix() {
        try {
            return objectMapper.readValue(data, int[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMatrix(int[][] matrix) {
        try {
            this.data = objectMapper.writeValueAsString(matrix);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing matrix data", e);
        }
    }
}