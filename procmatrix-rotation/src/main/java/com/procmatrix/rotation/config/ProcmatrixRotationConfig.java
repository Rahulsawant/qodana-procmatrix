package com.procmatrix.rotation.config;

import com.procmatrix.core.config.MatrixCacheConfig;
import com.procmatrix.core.config.MatrixDatabaseConfig;
import com.procmatrix.core.config.SecurityConfig;
import com.procmatrix.core.config.UserDetailsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MatrixDatabaseConfig.class, MatrixCacheConfig.class , SecurityConfig.class, UserDetailsConfig.class})
public class ProcmatrixRotationConfig {
}
