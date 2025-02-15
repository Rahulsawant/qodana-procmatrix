package com.qodana.matrixrotation.entity;

import com.procmatrix.core.entity.MatrixRequest;

public class RotateMatrixRequest extends MatrixRequest {
    private int degree;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}