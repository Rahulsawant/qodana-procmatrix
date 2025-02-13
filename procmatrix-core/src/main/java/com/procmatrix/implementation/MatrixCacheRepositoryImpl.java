package com.procmatrix.implementation;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.interfaces.repository.MatrixCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

@Repository
public class MatrixCacheRepositoryImpl implements MatrixCacheRepository<Long,MatrixData> {

    private final CacheManager cacheManager;
    @Autowired
    public MatrixCacheRepositoryImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    @Override
    public MatrixData get(Long id) {
        Object cacheValue = cacheManager.getCache("matrixCache").get(id);
        if (cacheValue instanceof org.springframework.cache.support.SimpleValueWrapper) {
            return (MatrixData) ((org.springframework.cache.support.SimpleValueWrapper) cacheValue).get();
        }
        return (MatrixData) cacheValue;
    }

    @Override
    public void save(Long id, MatrixData matrix) {
        this.cacheManager.getCache("matrixCache").putIfAbsent(id, matrix);
    }

    @Override
    public boolean remove(Long id) {
        return cacheManager.getCache("matrixCache").evictIfPresent(id);
    }
}
