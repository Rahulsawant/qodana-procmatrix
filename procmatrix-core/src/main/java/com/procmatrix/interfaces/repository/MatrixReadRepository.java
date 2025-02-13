package com.procmatrix.interfaces.repository;

public interface MatrixReadRepository<ID,T>{
    Iterable<T> findAll();
    T findById(ID id);
    boolean existsById(ID id);
    long count();
}
