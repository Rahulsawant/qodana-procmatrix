package com.qodana.matrixrotation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({com.procmatrix.config.MatrixDatabaseConfig.class, com.procmatrix.config.MatrixCacheConfig.class})
public class ProcmatrixRotationConfig {
}
