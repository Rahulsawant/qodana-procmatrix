package com.qodana.matrixrotation.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatrixRotationService {

    @Autowired
    private MatrixCacheRepository<Long,MatrixData> matrixCacheRepository;
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
        MatrixData matrix = matrixCacheRepository.get(id);
        if (matrix == null) {
            matrix = matrixReadRepository.findById(id);
            if (matrix != null) {
                matrixCacheRepository.save(id, matrix);
            }
        }
        if (matrix == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrix not found");
        }
        return matrix.getMatrix();
    }
}
