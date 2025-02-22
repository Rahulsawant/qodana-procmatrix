package com.procmatrix.core.interfaces.repository;


public interface MatrixWriteRepository<ID,T>{
    T persist(T object);
    boolean delete(ID id);
}
