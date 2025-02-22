package com.procmatrix.core.interfaces.services;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;

public interface MatrixRetrievalService {
    int[][] getMatrix(Long id);
}

interface MatrixSaveService {
    MatrixData saveMatrix(MatrixRequest matrix);
}

interface MatrixDeleteService {
    boolean deleteMatrix(Long id);
}

interface MatrixAdditionService {
    int[][] addMatrices(int[][] matrix1, int[][] matrix2);
}
