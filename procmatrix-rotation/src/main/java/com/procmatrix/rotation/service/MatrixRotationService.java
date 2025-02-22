package com.procmatrix.rotation.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.IntStream;

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


    /**
     * 1. Validate the degree to ensure it is a multiple of 90.
     * 2. Determine the dimensions of the rotated matrix.
     * 3. Use parallel streams to iterate through each element of the matrix and perform the rotation.
     * Rotation logic:
     * 1 For 90° and 270° rotation, swap the rows and columns.
     * 2 Iterate through each element:
     *   2.1 90°: The element at (i, j) in the original matrix moves to (j, rows - 1 - i) in the rotated matrix.
     *   2.2 180°: The element at (i, j) moves to (rows - 1 - i, cols - 1 - j).
     *   2.3 270°: The element at (i, j) moves to (cols - 1 - j, i).
     *   2.4 0° or 360°: The matrix remains unchanged.
     * @param matrix the matrix to rotate
     * @param degree the degree to rotate the matrix by
     * @return the rotated matrix
     */
    public int[][] rotateMatrix(int[][] matrix, int degree) {
        logger.debug("Rotating matrix by {} degrees", degree);
        try {
            if (degree % 90 != 0) {
                throw new IllegalArgumentException("Degree must be a multiple of 90");
            }
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[][] rotatedMatrix;
            if (degree == 90 || degree == 270) {
                rotatedMatrix = new int[cols][rows];
            } else {
                rotatedMatrix = new int[rows][cols];
            }

            IntStream.range(0, rows).parallel().forEach(i -> {
                for (int j = 0; j < cols; j++) {
                    if (degree == 90) {
                        rotatedMatrix[j][rows - 1 - i] = matrix[i][j];
                    } else if (degree == 180) {
                        rotatedMatrix[rows - 1 - i][cols - 1 - j] = matrix[i][j];
                    } else if (degree == 270) {
                        rotatedMatrix[cols - 1 - j][i] = matrix[i][j];
                    } else {
                        rotatedMatrix[i][j] = matrix[i][j];
                    }
                }
            });
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