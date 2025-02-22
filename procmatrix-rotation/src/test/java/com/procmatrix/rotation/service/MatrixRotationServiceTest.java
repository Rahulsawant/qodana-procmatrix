package com.procmatrix.rotation.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.repository.MatrixCacheRepository;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatrixRotationServiceTest {

    @Mock
    private MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;

    @Mock
    private MatrixWriteRepository<Long, MatrixData> matrixWriteRepository;

    @Mock
    private MatrixReadRepository<Long, MatrixData> matrixReadRepository;

    @InjectMocks
    private MatrixRotationService matrixRotationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMatrix_returnsMatrix() {
        Long matrixId = 1L;
        MatrixData matrixData = new MatrixData();
        matrixData.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixCacheRepository.get(matrixId)).thenReturn(matrixData);

        int[][] result = matrixRotationService.getMatrix(matrixId);

        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, result);
    }


    @Test
    void saveMatrix_savesAndReturnsMatrix() {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        MatrixData matrixData = new MatrixData();
        matrixData.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixWriteRepository.persist(any(MatrixData.class))).thenReturn(matrixData);

        MatrixData result = matrixRotationService.saveMatrix(matrixRequest);

        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, result.getMatrix());
    }

    @Test
    void saveMatrix_throwsExceptionOnError() {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixWriteRepository.persist(any(MatrixData.class))).thenThrow(new RuntimeException());

        assertThrows(ResponseStatusException.class, () -> matrixRotationService.saveMatrix(matrixRequest));
    }

    @Test
    void rotateMatrix_rotatesMatrixBy90Degrees() {
        int[][] matrix = {{1, 2}, {3, 4}};
        int[][] expected = {{3, 1}, {4, 2}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 90);

        assertArrayEquals(expected, result);
    }

    @Test
    void rotateMatrix_rotatesMatrixBy180Degrees() {
        int[][] matrix = {{1, 2}, {3, 4}};
        int[][] expected = {{4, 3}, {2, 1}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 180);

        assertArrayEquals(expected, result);
    }

    @Test
    void rotateMatrix_rotatesMatrixBy270Degrees() {
        int[][] matrix = {{1, 2}, {3, 4}};
        int[][] expected = {{2, 4}, {1, 3}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 270);

        assertArrayEquals(expected, result);
    }

    @Test
    void rotateMatrix_throwsExceptionForInvalidDegree() {
        int[][] matrix = {{1, 2}, {3, 4}};

        assertThrows(IllegalArgumentException.class, () -> matrixRotationService.rotateMatrix(matrix, 45));
    }

    @Test
    void rotateMatrix_rotatesMatrixBy90Degrees_3x2() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        int[][] expected = {{4, 1}, {5, 2}, {6, 3}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 90);

        assertArrayEquals(expected, result);
    }

    @Test
    void rotateMatrix_rotatesMatrixBy180Degrees_3x2() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        int[][] expected = {{6, 5, 4}, {3, 2, 1}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 180);

        assertArrayEquals(expected, result);
    }

    @Test
    void rotateMatrix_rotatesMatrixBy270Degrees_3x2() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        int[][] expected = {{3, 6}, {2, 5}, {1, 4}};

        int[][] result = matrixRotationService.rotateMatrix(matrix, 270);

        assertArrayEquals(expected, result);
    }

}