package com.procmatrix.core.utils;

import com.procmatrix.core.entity.MatrixRequest;

public class InputValidator {

    public static void validateMatrixRequest(MatrixRequest request) {
        if (request == null || request.getMatrix() == null) {
            throw new IllegalArgumentException("MatrixRequest or matrix cannot be null");
        }

        int[][] matrix = request.getMatrix();
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Matrix cannot be empty");
        }

        int rowLength = matrix[0].length;
        for (int[] row : matrix) {
            if (row.length != rowLength) {
                throw new IllegalArgumentException("All rows in the matrix must have the same length");
            }
        }
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer");
        }
    }
}
