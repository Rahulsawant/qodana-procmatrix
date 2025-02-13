package com.procmatrix.implementation;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.interfaces.repository.MatrixCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MatrixCacheRepositoryImpl implements MatrixCacheRepository<Long,MatrixData> {
    @Autowired
    private RedisTemplate<Long, MatrixData> redisTemplate;

    @Override
    public MatrixData getMatrix(Long id) {
        return redisTemplate.opsForValue().get(id);
    }

    @Override
    public void saveMatrix(Long id, MatrixData matrix) {
        redisTemplate.opsForValue().set(id, matrix);
    }

    @Override
    public boolean deleteMatrix(Long id) {
        return redisTemplate.delete(id);
    }
}
