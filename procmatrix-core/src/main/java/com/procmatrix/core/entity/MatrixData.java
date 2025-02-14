package com.procmatrix.core.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;

@Entity
//@Document(collection = "matrix_entity") // TODO for noSql
public class MatrixData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String data;

    @Transient
    private int[][] matrix;

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
        if (matrix == null && data != null) {
            try {
                matrix = objectMapper.readValue(data, int[][].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
        try {
            this.data = objectMapper.writeValueAsString(matrix);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
