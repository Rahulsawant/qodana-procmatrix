package com.procmatrix.core.interfaces;


public interface MatrixCacheRepository<ID,T> {
    T get(ID id);
    void save(ID id, T matrix);
    boolean remove(ID id);
}

