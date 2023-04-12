package com.kenzie.capstone.service.caching;

import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class CacheClient {
    public CacheClient() {
        //  Jedis cache = DaggerServiceComponent.create().provideJedis();
    }

    public void setValue(String key, int seconds, String value) {
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        if(key != null) {
            cache.setex(key, seconds, value);
        }
        cache.close();
        // Check for non-null key
        // Set the value in the cache
    }
    public Optional<String> getValue(String key) {
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        String response = new String();
        if(key != null) {
            response = cache.get(key);
            cache.close();
            return Optional.ofNullable(response);
        }
        cache.close();
        return Optional.empty();
        // Check for non-null key
        // Retrieves the Optional values from the cache
    }
    public void invalidate(String key) {
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        if(key != null){
            cache.del(key);
        }
        cache.close();
        // Check for non-null key
        // Delete the key
    }
    private void checkNonNullKey(String key) throws Exception {
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        if (key == null){
            throw new Exception("Key is null");
        }
        cache.close();
        // Ensure the key isn't null
        // What should you do if the key *is* null?
    }

    // Put your Cache Client Here

    // Since Jedis is being used multithreaded, you MUST get a new Jedis instances and close it inside every method.
    // Do NOT use a single instance across multiple of these methods

    // Use Jedis in each method by doing the following:
    // Jedis cache = DaggerServiceComponent.create().provideJedis();
    // ... use the cache
    // cache.close();

    // Remember to check for null keys!
}
