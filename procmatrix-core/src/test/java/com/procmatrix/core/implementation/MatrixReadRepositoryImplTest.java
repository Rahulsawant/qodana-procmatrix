package com.procmatrix.core.implementation;

import com.procmatrix.core.config.TestConfig;
import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.utils.MatrixSqlStatements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
@ComponentScan(basePackages = "com.procmatrix.core.*")
class MatrixReadRepositoryImplTest {

    @MockitoBean
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private MatrixReadRepositoryImpl matrixReadRepository;

    @Test
    void testFindById_Success() {
        MatrixData expectedMatrixData = new MatrixData();
        expectedMatrixData.setId(1L);
        expectedMatrixData.setData("test data");

        when(namedJdbcTemplate.queryForObject(eq(MatrixSqlStatements.FIND_BY_ID), anyMap(), any(MatrixReadRepositoryImpl.MatrixDataRowMapper.class)))
                .thenReturn(expectedMatrixData);

        MatrixData result = matrixReadRepository.findById(1L);

        assertNotNull(result);
        assertEquals(expectedMatrixData.getId(), result.getId());
        assertEquals(expectedMatrixData.getData(), result.getData());
    }

    @Test
    void testFindById_EmptyResultDataAccessException() {
        when(namedJdbcTemplate.queryForObject(eq(MatrixSqlStatements.FIND_BY_ID), anyMap(), any(MatrixReadRepositoryImpl.MatrixDataRowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        MatrixData result = matrixReadRepository.findById(1L);

        assertNull(result);
    }

    @Test
    void testFindById_DataAccessException() {
        when(namedJdbcTemplate.queryForObject(eq(MatrixSqlStatements.FIND_BY_ID), anyMap(), any(MatrixReadRepositoryImpl.MatrixDataRowMapper.class)))
                .thenThrow(new DataAccessException("error") {});

        MatrixData result = matrixReadRepository.findById(1L);

        assertNull(result);
    }
}