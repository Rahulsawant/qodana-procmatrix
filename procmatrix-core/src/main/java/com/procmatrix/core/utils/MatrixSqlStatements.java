package com.procmatrix.core.utils;

public class MatrixSqlStatements {
    public static final String FIND_BY_ID = "SELECT * FROM matrix_data WHERE id = :id";
    public static final String INSERT_MATRIX_DATA = "INSERT INTO matrix_data (data) VALUES (:data)";
    public static final String DELETE_BY_ID = "DELETE FROM matrix_data WHERE id = :id";
}