package com.procmatrix.core.config;


import com.procmatrix.core.implementation.MatrixCacheRepositoryImpl;
import com.procmatrix.core.implementation.MatrixReadRepositoryImpl;
import com.procmatrix.core.implementation.MatrixWriteRepositoryImpl;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/*import javax.sql.DataSource;*/

@Configuration
@ComponentScan(basePackages = "com.procmatrix.core.*")
public class TestConfig {

    @Bean
    public MatrixReadRepositoryImpl matrixReadRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        return new MatrixReadRepositoryImpl(namedJdbcTemplate);
    }

    @Bean
    public MatrixWriteRepositoryImpl matrixWriteRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        return new MatrixWriteRepositoryImpl(namedJdbcTemplate);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("matrixCache");
    }
    @Bean
    public MatrixCacheRepositoryImpl matrixCacheRepository(CacheManager cacheManager) {
        return new MatrixCacheRepositoryImpl(cacheManager);
    }
}