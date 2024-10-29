package com.ioh.repository.impl;




import com.ioh.repository.RedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static com.ioh.utility.GeneralUtils.*;


@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public boolean isKeyExist(String key) {
        return redisTemplate.hasKey(getPrefixedKey(key));
    }

    @Override
    public void save(String key, String data, Date expiryDate) {
        key = getPrefixedKey(key);
        valueOperations.set(key, data);
        redisTemplate.expireAt(key, expiryDate);
    }

    @Override
    public void delete(String key) {
        key = getPrefixedKey(key);
        redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        key = getPrefixedKey(key);
        return valueOperations.get(key);
    }

    @Override
    public void deleteEntryWithApiKey() {
        valueOperations.getOperations().keys("*api-key-detail*")
                .forEach(key -> redisTemplate.delete(key));
    }

    private String getPrefixedKey(String key) {
        return PROJECT_NAME_PREFIX + HYPHEN + "dev" + HYPHEN + key;
    }

}