package com.procmatrix.core.implementation;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.repository.MatrixWriteRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Types;

@Service
public class MatrixWriteRepositoryImpl implements MatrixWriteRepository<Long, MatrixData> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MatrixWriteRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public MatrixData persist(MatrixData matrixData) {
        String sql = "INSERT INTO matrix_data (data) VALUES (:data)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("data", new SqlLobValue(matrixData.getData().getBytes()), Types.BLOB);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        matrixData.setId(keyHolder.getKey().longValue());
        return matrixData;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM matrix_data WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }
}