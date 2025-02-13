package com.procmatrix.implementation;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.interfaces.repository.MatrixReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;


@Service
public class MatrixReadRepositoryImpl implements MatrixReadRepository<Long,MatrixData> {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Override
    public Iterable<MatrixData> findAll() {
        return cassandraTemplate.select("SELECT * FROM matrix_data", MatrixData.class);
    }

    @Override
    public MatrixData findById(Long id) {
        return cassandraTemplate.selectOneById(id, MatrixData.class);
    }

    @Override
    public boolean existsById(Long id) {
        return cassandraTemplate.exists(id, MatrixData.class);
    }

    @Override
    public long count() {
        return cassandraTemplate.count(MatrixData.class);
    }
}