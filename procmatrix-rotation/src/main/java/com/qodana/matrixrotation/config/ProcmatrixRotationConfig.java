package com.qodana.matrixrotation.config;

import com.procmatrix.core.config.MatrixCacheConfig;
import com.procmatrix.core.config.MatrixDatabaseConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MatrixDatabaseConfig.class, MatrixCacheConfig.class})
public class ProcmatrixRotationConfig {
}
