package com.procmatrix.interfaces.repository;


public interface MatrixCacheRepository<ID,T> {
    T getMatrix(ID id);
    void saveMatrix(ID id, T matrix);
    boolean deleteMatrix(ID id);
}

