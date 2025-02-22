package com.procmatrix.core.implementation;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import com.procmatrix.core.utils.MatrixSqlStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static com.procmatrix.core.utils.MatrixExceptionBuilder.ExceptionMessages.ERROR_DATA_NOT_FOUND;
import static com.procmatrix.core.utils.MatrixExceptionBuilder.ExceptionMessages.ERROR_FINDING_MATRIX;

@Repository
public class MatrixReadRepositoryImpl implements MatrixReadRepository<Long, MatrixData> {

    private static final Logger logger = LoggerFactory.getLogger(MatrixReadRepositoryImpl.class);


    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MatrixReadRepositoryImpl(@Qualifier("customJdbcTemplate") NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    protected static final class MatrixDataRowMapper implements RowMapper<MatrixData> {
        @Override
        public MatrixData mapRow(ResultSet rs, int rowNum) throws SQLException {
            MatrixData matrixData = new MatrixData();
            matrixData.setId(rs.getLong("id"));
            matrixData.setData(new String(rs.getBytes("data")));
            return matrixData;
        }
    }

    @Override
    public MatrixData findById(Long id) {
        try {
            return namedJdbcTemplate.queryForObject(MatrixSqlStatements.FIND_BY_ID, Collections.singletonMap("id", id), new MatrixDataRowMapper());
        }catch(EmptyResultDataAccessException e){
            logger.debug(String.format(ERROR_DATA_NOT_FOUND ,id, e.getMessage()));
            return null;
        } catch (DataAccessException e  ) {
            logger.error(String.format(ERROR_FINDING_MATRIX, id, e));
            return null;
        }
    }

}