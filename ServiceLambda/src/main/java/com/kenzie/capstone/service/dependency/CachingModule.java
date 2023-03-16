package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.caching.CacheClient;
import dagger.Module;
import dagger.Provides;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class CachingModule {
    // DO NOT CHANGE ANYTHING IN THIS FILE!
    // This file is set up to automatically select the correct Jedis URL based upon your current
    // deployment configuration.  If you change anything here you risk breaking the connection to Jedis.

    /**
     * This method provides a JedisPool object used to connect to a Redis cache.
     * @return A JedisPool object
     */
    @Provides
    public static Jedis provideJedis() {
        String redisUrl = System.getenv("JEDIS_URL");
        if (redisUrl != null && redisUrl.length() > 0) {
            // Connect to AWS
            System.out.println("Providing redis " + redisUrl);
            return new Jedis(redisUrl, 6379, 20000);
        } else if ("true".equals(System.getenv("AWS_SAM_LOCAL"))) {
            // Connect to local Docker redis
            JedisPool pool = new JedisPool(new JedisPoolConfig(), "redis-stack", 6379, 20000);
            try {
                return pool.getResource();
            } catch (Exception e) {
                throw new IllegalStateException("Could not connect to the local redis container in docker.  " +
                        "Make sure that it is running and that you have configured the SAM CLI - Docker Network " +
                        "property to contain kenzie-local inside of your run configuration.");
            }
        } else {
            // Run Locally
            System.out.println("Providing local redis");
            return new JedisPool(new JedisPoolConfig(), "localhost", 6379, 20000).getResource();
        }
    }

    @Provides
    @Singleton
    @Named("CacheClient")
    public CacheClient provideCacheClient() {
        return new CacheClient();
    }
}
