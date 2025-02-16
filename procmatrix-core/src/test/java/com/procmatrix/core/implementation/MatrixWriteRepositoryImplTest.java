package com.procmatrix.core.implementation;

import com.procmatrix.core.config.TestConfig;
import com.procmatrix.core.entity.MatrixData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
class MatrixWriteRepositoryImplTest {

    @MockitoBean
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private MatrixWriteRepositoryImpl matrixWriteRepository;

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(2);
            keyHolder.getKeyList().add(Collections.singletonMap("id", 1L));
            return 1;
        }).when(namedJdbcTemplate).update(any(String.class), any(MapSqlParameterSource.class), any(KeyHolder.class), any(String[].class));
    }

    @Test
    void testPersist_Success() {
        MatrixData matrixData = new MatrixData();
        matrixData.setData("test data");

        MatrixData result = matrixWriteRepository.persist(matrixData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testPersist_DataAccessException() {
        MatrixData matrixData = new MatrixData();
        matrixData.setData("test data");

        doThrow(new DataAccessException("error") {}).when(namedJdbcTemplate).update(any(String.class), any(MapSqlParameterSource.class), any(KeyHolder.class), any(String[].class));

        MatrixData result = matrixWriteRepository.persist(matrixData);

        assertNull(result);
    }

    @Test
    void testDelete_Success() {
        when(namedJdbcTemplate.update(any(String.class), any(MapSqlParameterSource.class))).thenReturn(1);

        boolean result = matrixWriteRepository.delete(1L);

        assertTrue(result);
    }

    @Test
    void testDelete_DataAccessException() {
        doThrow(new DataAccessException("error") {}).when(namedJdbcTemplate).update(any(String.class), any(MapSqlParameterSource.class));

        boolean result = matrixWriteRepository.delete(1L);

        assertFalse(result);
    }
}