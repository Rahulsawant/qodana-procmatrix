package com.procmatrix.core.implementation;

import com.procmatrix.core.config.TestConfig;
import com.procmatrix.core.entity.MatrixData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
class MatrixCacheRepositoryImplTest {

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private Cache cache;

    @Autowired
    private MatrixCacheRepositoryImpl matrixCacheRepository;

    /*@BeforeEach
    void setUp() {
        when(cacheManager.getCache("matrixCache")).thenReturn(cache);
    }*/

    @Test
    void testGet_Success() {
        MatrixData expectedMatrixData = new MatrixData();
        expectedMatrixData.setId(1L);
        expectedMatrixData.setData("test data");

        when(cache.get(1L)).thenReturn(() -> expectedMatrixData);

        MatrixData result = matrixCacheRepository.get(1L);

        assertNotNull(result);
        assertEquals(expectedMatrixData.getId(), result.getId());
        assertEquals(expectedMatrixData.getData(), result.getData());
    }

    @Test
    void testGet_DataAccessException() {
        when(cache.get(1L)).thenThrow(new DataAccessException("error") {});

        MatrixData result = matrixCacheRepository.get(1L);

        assertNull(result);
    }

    @Test
    void testSave_Success() {
        MatrixData matrixData = new MatrixData();
        matrixData.setId(1L);
        matrixData.setData("test data");

        matrixCacheRepository.save(1L, matrixData);

        verify(cache, times(1)).putIfAbsent(1L, matrixData);
    }

    @Test
    void testSave_DataAccessException() {
        MatrixData matrixData = new MatrixData();
        matrixData.setId(1L);
        matrixData.setData("test data");

        doThrow(new DataAccessException("error") {}).when(cache).putIfAbsent(1L, matrixData);

        matrixCacheRepository.save(1L, matrixData);

        verify(cache, times(1)).putIfAbsent(1L, matrixData);
    }

    @Test
    void testRemove_Success() {
        when(cache.evictIfPresent(1L)).thenReturn(true);

        boolean result = matrixCacheRepository.remove(1L);

        assertTrue(result);
    }

    @Test
    void testRemove_DataAccessException() {
        doThrow(new DataAccessException("error") {}).when(cache).evictIfPresent(1L);

        boolean result = matrixCacheRepository.remove(1L);

        assertFalse(result);
    }
}