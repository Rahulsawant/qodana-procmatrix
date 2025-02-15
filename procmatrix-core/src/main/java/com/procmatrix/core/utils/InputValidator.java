package com.procmatrix.core.utils;

import com.procmatrix.core.entity.MatrixRequest;

import static com.procmatrix.core.utils.MatrixExceptionBuilder.ExceptionMessages.*;

public class InputValidator {

    public static void validateMatrixRequest(MatrixRequest request) {
        if (request == null || request.getMatrix() == null) {
            throw new IllegalArgumentException(MATRIX_REQUEST_NULL);
        }

        int[][] matrix = request.getMatrix();
        if (matrix.length == 0) {
            throw new IllegalArgumentException(MATRIX_EMPTY);
        }

        int rowLength = matrix[0].length;
        for (int[] row : matrix) {
            if (row.length != rowLength) {
                throw new IllegalArgumentException(MATRIX_ROWS_LENGTH_MISMATCH);
            }
        }
    }
    public static void validateMatrixRequests(MatrixRequest... requests) {
        for (MatrixRequest request : requests) {
            validateMatrixRequest(request);
        }
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(ID_NULL);
        }
        if (id <= 0) {
            throw new IllegalArgumentException(ID_INVALID);
        }
    }

    public static void validateIds(Long... ids) {
        for (Long id : ids) {
            validateId(id);
        }
    }
}