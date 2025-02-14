package com.procmatrix.core.implementation;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.repository.MatrixReadRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@Repository
public class MatrixReadRepositoryImpl implements MatrixReadRepository<Long, MatrixData> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MatrixReadRepositoryImpl(@Qualifier("customJdbcTemplate")  NamedParameterJdbcTemplate  namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private static final class MatrixDataRowMapper implements RowMapper<MatrixData> {
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
        String sql = "SELECT * FROM matrix_data WHERE id = :id";
        return namedJdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), new MatrixDataRowMapper());

    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(1) FROM matrix_data WHERE id = ?";
        Integer count = namedJdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), Integer.class);
        return count > 0;
    }
}
