package com.ioh.repository;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface RedisRepository {
    boolean isKeyExist(String key);

    void save(String key, String Data, Date expiryDate);

    void delete(String key);

    String get(String key);

    void deleteEntryWithApiKey();
}