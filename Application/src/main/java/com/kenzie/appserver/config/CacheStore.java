package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.VideoGame;


import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, VideoGame> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initialize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();    
    }

    public VideoGame get(String key) {
        // Write your code here
        // Retrieve and return the videogame
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the videogame from cache
        cache.invalidate(key);
    }

    public void add(String key, VideoGame value) {
        // Write your code here
        // Add videogame to cache
        cache.put(key,value);
    }
}
