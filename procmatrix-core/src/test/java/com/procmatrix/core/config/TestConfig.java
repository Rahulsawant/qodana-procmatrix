package com.procmatrix.core.config;


import com.procmatrix.core.implementation.MatrixReadRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/*import javax.sql.DataSource;*/

@Configuration
public class TestConfig {

    /*@Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }*/

    @Bean
    public MatrixReadRepositoryImpl matrixReadRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        return new MatrixReadRepositoryImpl(namedJdbcTemplate);
    }
}