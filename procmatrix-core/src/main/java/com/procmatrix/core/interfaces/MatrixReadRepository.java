package com.procmatrix.core.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatrixReadRepository<ID,T>{
    T findById(ID id);
}
