package com.kenzie.capstone.service.dependency;


import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.caching.CachingVideoGameDao;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    @Named("DynamoDBMapper")
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

    @Singleton
    @Provides
    @Named("VideoGameDao")
    @Inject
    public VideoGameDao provideVideoGameDao(
            @Named("CacheClient") CacheClient cacheClient,
            @Named("NonCachingVideoGameDao") NonCachingVideoGameDao nonCachingVideoGameDao) {
        return new CachingVideoGameDao(cacheClient, nonCachingVideoGameDao);
    }
    @Singleton
    @Provides
    @Named("NonCachingVideoGameDao")
    @Inject
    public NonCachingVideoGameDao provideNonCachingVideoGameDao(@Named("DynamoDBMapper") DynamoDBMapper mapper) {
        return new NonCachingVideoGameDao(mapper);
    }

}
