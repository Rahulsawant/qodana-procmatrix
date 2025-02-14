package com.qodana.procmatrix.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing matrix data.
 */
@Service
public class MatrixService {
    private final MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;
    private final MatrixWriteRepository<Long,MatrixData> matrixWriteRepository;
    private final MatrixReadRepository<Long,MatrixData> matrixReadRepository;

    @Autowired
    public MatrixService(MatrixCacheRepository<Long, MatrixData> matrixCacheRepository,
                         MatrixWriteRepository<Long, MatrixData> matrixWriteRepository,
                         MatrixReadRepository<Long, MatrixData> matrixReadRepository) {
        this.matrixCacheRepository = matrixCacheRepository;
        this.matrixWriteRepository = matrixWriteRepository;
        this.matrixReadRepository = matrixReadRepository;
    }


    /**
     * Retrieves a matrix by its ID.
     * First checks the Redis cache, and if not found, checks Cassandra.
     * If found in Cassandra, updates the Redis cache.
     *
     * @param id the ID of the matrix
     * @return the matrix data, or null if not found
     */
    public int[][] getMatrix(Long id) {
        // First, check the Redis cache
        MatrixData matrix = matrixCacheRepository.get(id);
        if (matrix == null) {
            matrix= matrixReadRepository.findById(id);
            if (matrix != null) {
                matrixCacheRepository.save(id, matrix);
            }
        }
        return matrix.getMatrix();
    }

    /**
     * Saves a matrix with the given ID.
     * Persists the matrix to Cassandra and updates the Redis cache.
     *
     * @param matrix the matrix data
     */
    @Transactional
    //TODO : need to check if we can first store to cache & then DB
    public MatrixData saveMatrix(MatrixRequest matrix) {
        // Save to Cassandra
        MatrixData matrixData=new MatrixData();
        matrixData.setMatrix(matrix.getMatrix());
        matrixData=matrixWriteRepository.persist(matrixData);
        // Update the Redis cache
        matrixCacheRepository.save(matrixData.getId(), matrixData);
        return matrixData;
    }

    /**
     * Deletes a matrix by its ID.
     * Removes the matrix from the Redis cache and deletes it from Cassandra.
     *
     * @param id the ID of the matrix
     */
    @Transactional
    public boolean deleteMatrix(Long id) {
        matrixCacheRepository.remove(id);
        return matrixWriteRepository.delete(id);
    }
}