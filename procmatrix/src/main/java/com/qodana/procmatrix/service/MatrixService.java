package com.qodana.procmatrix.service;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.entity.MatrixRequest;
import com.procmatrix.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.interfaces.repository.MatrixReadRepository;
import com.procmatrix.interfaces.repository.MatrixWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing matrix data.
 */
@Service
public class MatrixService {
    @Autowired
    private MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;
    @Autowired
    private MatrixWriteRepository<Long,MatrixData> matrixWriteRepository;
    @Autowired
    private MatrixReadRepository<Long,MatrixData> matrixReadRepository;

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
            matrix = matrixReadRepository.findById(id);
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