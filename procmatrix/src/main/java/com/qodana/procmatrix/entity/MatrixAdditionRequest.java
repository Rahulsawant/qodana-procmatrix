package com.qodana.procmatrix.entity;

import com.procmatrix.core.entity.MatrixRequest;

public class MatrixAdditionRequest {
    private MatrixRequest matrix1;
    private MatrixRequest matrix2;

    public MatrixRequest getMatrix1() {
        return matrix1;
    }

    public void setMatrix1(MatrixRequest matrix1) {
        this.matrix1 = matrix1;
    }

    public MatrixRequest getMatrix2() {
        return matrix2;
    }

    public void setMatrix2(MatrixRequest matrix2) {
        this.matrix2 = matrix2;
    }
}