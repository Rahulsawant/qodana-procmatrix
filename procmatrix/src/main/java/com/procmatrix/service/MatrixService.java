package com.procmatrix.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

/**
 * Service class for managing matrix data.
 */
@Service
public class MatrixService {
    private static final Logger logger = LoggerFactory.getLogger(MatrixService.class);

    private final MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;
    private final MatrixWriteRepository<Long, MatrixData> matrixWriteRepository;
    private final MatrixReadRepository<Long, MatrixData> matrixReadRepository;

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
        logger.debug("Retrieving matrix with ID {}", id);
        try {
            MatrixData matrix = matrixCacheRepository.get(id);
            if (matrix == null) {
                // If not found in cache, check Cassandra
                matrix = matrixReadRepository.findById(id);
                if (matrix != null) {
                    // Update the cache
                    matrixCacheRepository.save(id, matrix);
                } else {
                    logger.warn("Matrix with ID {} not found in cache or database", id);
                    return null;
                }
            }
            logger.debug("Successfully retrieved matrix with ID {}", id);
            return matrix.getMatrix();
        } catch (Exception e) {
            logger.error("Error retrieving matrix with ID {}", id, e);
            return null;
        }
    }

    /**
     * Saves a matrix.
     *
     * @param matrix the matrix to save
     * @return the saved matrix data
     */
    @Transactional
    public MatrixData saveMatrix(MatrixRequest matrix) {
        logger.debug("Saving matrix");
        try {
            MatrixData matrixData = new MatrixData();
            matrixData.setMatrix(matrix.getMatrix());
            MatrixData savedMatrix = matrixWriteRepository.persist(matrixData);
            if (savedMatrix != null) {
                matrixCacheRepository.save(savedMatrix.getId(), savedMatrix);
            }
            logger.debug("Successfully saved matrix with ID {}", matrixData.getId());
            return savedMatrix;
        } catch (Exception e) {
            logger.error("Error saving matrix", e);
            return null;
        }
    }

    /**
     * Deletes a matrix by its ID.
     *
     * @param id the ID of the matrix
     * @return true if the matrix was deleted, false otherwise
     */
    @Transactional
    public boolean deleteMatrix(Long id) {
        logger.debug("Deleting matrix with ID {}", id);
        try {
            matrixCacheRepository.remove(id);
            return matrixWriteRepository.delete(id);
        } catch (Exception e) {
            logger.error("Error deleting matrix with ID {}", id, e);
            return false;
        }
    }

    /**
     * Adds two matrices, handling matrices of different dimensions by filling missing elements with zeros.
     * @param matrix1 the first matrix to add
     * @param matrix2 the second matrix to add
     * @return the resulting matrix after addition
     */
    public int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int rows = Math.max(matrix1.length, matrix2.length);
        int cols = Math.max(matrix1[0].length, matrix2[0].length);
        int[][] result = new int[rows][cols];

        IntStream.range(0, rows).parallel().forEach(i -> {
            for (int j = 0; j < cols; j++) {
                int val1 = (i < matrix1.length && j < matrix1[i].length) ? matrix1[i][j] : 0;
                int val2 = (i < matrix2.length && j < matrix2[i].length) ? matrix2[i][j] : 0;
                result[i][j] = val1 + val2;
            }
        });
        return result;
    }
}