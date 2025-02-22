package com.procmatrix.core.implementation;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Types;

import static com.procmatrix.core.utils.MatrixExceptionBuilder.ExceptionMessages.*;
import static com.procmatrix.core.utils.MatrixSqlStatements.DELETE_BY_ID;
import static com.procmatrix.core.utils.MatrixSqlStatements.INSERT_MATRIX_DATA;

@Service
public class MatrixWriteRepositoryImpl implements MatrixWriteRepository<Long, MatrixData> {

    private static final Logger logger = LoggerFactory.getLogger(MatrixWriteRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MatrixWriteRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public MatrixData persist(MatrixData matrixData) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("data", new SqlLobValue(matrixData.getData().getBytes()), Types.BLOB);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedJdbcTemplate.update(INSERT_MATRIX_DATA, params, keyHolder, new String[]{"id"});
            matrixData.setId(keyHolder.getKey().longValue());
            return matrixData;
        } catch (DataAccessException e) {
            logger.error(String.format(ERROR_INSERTING_MATRIX, matrixData, e.getMessage()), e);
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            int rowsAffected = namedJdbcTemplate.update(DELETE_BY_ID, params);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            logger.error(String.format(ERROR_DELETING_MATRIX, id, e.getMessage()), e);
            return false;
        }
    }
}