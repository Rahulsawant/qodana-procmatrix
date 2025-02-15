package com.qodana.matrixrotation.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.MatrixCacheRepository;
import com.procmatrix.core.interfaces.MatrixReadRepository;
import com.procmatrix.core.interfaces.MatrixWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatrixRotationService {

    private final MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;
    private final MatrixWriteRepository<Long,MatrixData> matrixWriteRepository;
    private final MatrixReadRepository<Long,MatrixData> matrixReadRepository;

    @Autowired
    public MatrixRotationService(MatrixCacheRepository<Long, MatrixData> matrixCacheRepository,
                         MatrixWriteRepository<Long, MatrixData> matrixWriteRepository,
                         MatrixReadRepository<Long, MatrixData> matrixReadRepository) {
        this.matrixCacheRepository = matrixCacheRepository;
        this.matrixWriteRepository = matrixWriteRepository;
        this.matrixReadRepository = matrixReadRepository;
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
    public int[][] rotateMatrix(int[][] matrix, int degree) {
        int times = (degree / 90) % 4;
        for (int i = 0; i < times; i++) {
            matrix = rotate90Degrees(matrix);
        }
        return matrix;
    }

    private int[][] rotate90Degrees(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }
        return rotated;
    }
}
