package com.procmatrix.core.implementation;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.interfaces.MatrixCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import static com.procmatrix.core.utils.MatrixExceptionBuilder.ExceptionMessages.*;

@Repository
public class MatrixCacheRepositoryImpl implements MatrixCacheRepository<Long, MatrixData> {
    private static final Logger logger = LoggerFactory.getLogger(MatrixCacheRepositoryImpl.class);

    private final Cache cache;
    @Autowired
    public MatrixCacheRepositoryImpl(CacheManager cacheManager) {
        this.cache=cacheManager.getCache("matrixCache");
    }

    @Override
    public MatrixData get(Long id) {
        try {
            return (MatrixData) this.cache.get(id);
        } catch (DataAccessException | NullPointerException e) {
            logger.error(String.format(ERROR_GETTING_MATRIX_FROM_CACHE,id, e.getMessage()));
            return null;
        }
    }

    @Override
    public void save(Long id, MatrixData matrix) {
        try {
            this.cache.putIfAbsent(id, matrix);
        } catch (DataAccessException | NullPointerException e) {
            logger.error(String.format(ERROR_SAVING_MATRIX_TO_CACHE , id, e.getMessage()));
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            return this.cache.evictIfPresent(id);
        } catch (DataAccessException | NullPointerException e) {
            logger.error(String.format(ERROR_REMOVING_MATRIX_FROM_CACHE , id, e.getMessage()));
            return false;
        }
    }
}