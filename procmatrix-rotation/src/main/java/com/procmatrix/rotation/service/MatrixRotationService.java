package com.procmatrix.rotation.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.MatrixCacheRepository;
import com.procmatrix.core.interfaces.MatrixReadRepository;
import com.procmatrix.core.interfaces.MatrixWriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatrixRotationService {

    private static final Logger logger = LoggerFactory.getLogger(MatrixRotationService.class);

    private final MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;
    private final MatrixWriteRepository<Long, MatrixData> matrixWriteRepository;
    private final MatrixReadRepository<Long, MatrixData> matrixReadRepository;

    @Autowired
    public MatrixRotationService(MatrixCacheRepository<Long, MatrixData> matrixCacheRepository,
                                 MatrixWriteRepository<Long, MatrixData> matrixWriteRepository,
                                 MatrixReadRepository<Long, MatrixData> matrixReadRepository) {
        this.matrixCacheRepository = matrixCacheRepository;
        this.matrixWriteRepository = matrixWriteRepository;
        this.matrixReadRepository = matrixReadRepository;
    }

    public int[][] getMatrix(Long id) {
        logger.debug("Retrieving matrix with ID {}", id);
        try {
            MatrixData matrixData = matrixCacheRepository.get(id);
            if (matrixData == null) {
                matrixData = matrixReadRepository.findById(id);
                if (matrixData != null) {
                    matrixCacheRepository.save(id, matrixData);
                }
            }
            return matrixData != null ? matrixData.getMatrix() : null;
        } catch (Exception e) {
            logger.error("Error retrieving matrix with ID {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving matrix", e);
        }
    }

    @Transactional
    public MatrixData saveMatrix(MatrixRequest matrixRequest) {
        logger.debug("Saving matrix");
        try {
            MatrixData matrixData = new MatrixData();
            matrixData.setMatrix(matrixRequest.getMatrix());
            MatrixData savedMatrix = matrixWriteRepository.persist(matrixData);
            matrixCacheRepository.save(savedMatrix.getId(), savedMatrix);
            return savedMatrix;
        } catch (Exception e) {
            logger.error("Error saving matrix", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving matrix", e);
        }
    }


    public int[][] rotateMatrix(int[][] matrix, int degree) {
        logger.debug("Rotating matrix by {} degrees", degree);
        try {
            if (degree % 90 != 0) {
                throw new IllegalArgumentException("Degree must be a multiple of 90");
            }
            int n = matrix.length;
            int[][] rotatedMatrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (degree == 90) {
                        rotatedMatrix[j][n - 1 - i] = matrix[i][j];
                    } else if (degree == 180) {
                        rotatedMatrix[n - 1 - i][n - 1 - j] = matrix[i][j];
                    } else if (degree == 270) {
                        rotatedMatrix[n - 1 - j][i] = matrix[i][j];
                    } else {
                        rotatedMatrix[i][j] = matrix[i][j];
                    }
                }
            }
            return rotatedMatrix;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid degree: {}", degree, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error rotating matrix", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error rotating matrix", e);
        }
    }
}