package com.procmatrix.core.utils;

public class MatrixExceptionBuilder {

    public static class ExceptionMessages {
        public static final String MATRIX_REQUEST_NULL = "MatrixRequest or matrix cannot be null";
        public static final String MATRIX_EMPTY = "Matrix cannot be empty";
        public static final String MATRIX_ROWS_LENGTH_MISMATCH = "All rows in the matrix must have the same length";
        public static final String ID_NULL = "ID cannot be null";
        public static final String ID_INVALID = "ID must be a positive integer";
        public static final String ERROR_FINDING_MATRIX = "Error finding matrix with id: %d Error: %s";
        public static final String ERROR_DATA_NOT_FOUND = "Data not found for matrix with id: %d Error: %s";
        public static final String ERROR_DELETING_MATRIX = "Error deleting matrix with id: %d Error: %s";
        public static final String ERROR_INSERTING_MATRIX = "Error inserting matrix with data: %s Error: %s";
        public static final String ERROR_GETTING_MATRIX_FROM_CACHE = "Error getting matrix from cache with id: %d Error: %s";
        public static final String ERROR_SAVING_MATRIX_TO_CACHE = "Error saving matrix to cache with id: %d Error: %s";
        public static final String ERROR_REMOVING_MATRIX_FROM_CACHE = "Error removing matrix from cache with id: %d Error: %s" ;
    }
}
