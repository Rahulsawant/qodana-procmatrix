package com.procmatrix.implementation;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.interfaces.repository.MatrixWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatrixWriteRepositoryImpl implements MatrixWriteRepository<Long,MatrixData> {

    @Autowired
    private CassandraTemplate cassandraTemplate;


    @Override
    public MatrixData persist(MatrixData matrixData) {
        return cassandraTemplate.insert(matrixData);
    }

    @Override
    public boolean delete(Long id) {
        UUID uuid = UUID.fromString(String.valueOf(id));
        return cassandraTemplate.deleteById(uuid, MatrixData.class);
    }
}