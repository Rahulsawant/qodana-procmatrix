package com.procmatrix.interfaces.repository;

public interface MatrixReadRepository<ID,T> {
    T findById(ID id);
    boolean existsById(ID id);
}
