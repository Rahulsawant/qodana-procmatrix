package com.qodana.procmatrix.service;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.interfaces.MatrixCacheRepository;
import com.procmatrix.core.interfaces.MatrixReadRepository;
import com.procmatrix.core.interfaces.MatrixWriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatrixServiceTest {

    @Mock
    private MatrixCacheRepository<Long, MatrixData> matrixCacheRepository;

    @Mock
    private MatrixWriteRepository<Long, MatrixData> matrixWriteRepository;

    @Mock
    private MatrixReadRepository<Long, MatrixData> matrixReadRepository;

    @InjectMocks
    private MatrixService matrixService;

    @Test
    void retrievesMatrixFromCache() {
        Long id = 1L;
        MatrixData matrixData = new MatrixData();
        matrixData.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixCacheRepository.get(id)).thenReturn(matrixData);

        int[][] result = matrixService.getMatrix(id);

        assertNotNull(result);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, result);
    }

    @Test
    void retrievesMatrixFromDatabaseWhenNotInCache() {
        Long id = 1L;
        MatrixData matrixData = new MatrixData();
        matrixData.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixCacheRepository.get(id)).thenReturn(null);
        when(matrixReadRepository.findById(id)).thenReturn(matrixData);

        int[][] result = matrixService.getMatrix(id);

        assertNotNull(result);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, result);
        verify(matrixCacheRepository).save(id, matrixData);
    }

    @Test
    void returnsNullWhenMatrixNotFound() {
        Long id = 1L;
        when(matrixCacheRepository.get(id)).thenReturn(null);
        when(matrixReadRepository.findById(id)).thenReturn(null);

        int[][] result = matrixService.getMatrix(id);

        assertNull(result);
    }

    @Test
    void savesMatrixSuccessfully() {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        MatrixData matrixData = new MatrixData();
        matrixData.setMatrix(matrixRequest.getMatrix());
        when(matrixWriteRepository.persist(any(MatrixData.class))).thenReturn(matrixData);

        MatrixData result = matrixService.saveMatrix(matrixRequest);

        assertNotNull(result);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, result.getMatrix());
        verify(matrixCacheRepository).save(result.getId(), result);
    }

    @Test
    void returnsNullWhenSaveMatrixFails() {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixWriteRepository.persist(any(MatrixData.class))).thenThrow(new RuntimeException("Save failed"));

        MatrixData result = matrixService.saveMatrix(matrixRequest);

        assertNull(result);
    }

    @Test
    void deletesMatrixSuccessfully() {
        Long id = 1L;
        when(matrixWriteRepository.delete(id)).thenReturn(true);

        boolean result = matrixService.deleteMatrix(id);

        assertTrue(result);
        verify(matrixCacheRepository).remove(id);
    }

    @Test
    void returnsFalseWhenDeleteMatrixFails() {
        Long id = 1L;
        when(matrixWriteRepository.delete(id)).thenReturn(false);

        boolean result = matrixService.deleteMatrix(id);

        assertFalse(result);
    }

    @Test
    void handlesExceptionWhenDeletingMatrix() {
        Long id = 1L;
        doThrow(new RuntimeException("Delete failed")).when(matrixWriteRepository).delete(id);

        boolean result = matrixService.deleteMatrix(id);

        assertFalse(result);
    }
}