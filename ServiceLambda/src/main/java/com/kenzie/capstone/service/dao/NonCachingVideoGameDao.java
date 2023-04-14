package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.VideoGameRecord;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;

public class NonCachingVideoGameDao implements VideoGameDao {
    private DynamoDBMapper mapper;
    private Logger log;

    public NonCachingVideoGameDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public VideoGameRecord addVideoGame(VideoGameRecord record) {
        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "name",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("Game has already been added");
        }
        return record;
    }

    @Override
    public boolean deleteVideoGame(VideoGameRecord record) {
        try {
            mapper.delete(record, new DynamoDBDeleteExpression()
                    .withExpected(ImmutableMap.of(
                            "name",
                            new ExpectedAttributeValue().withValue(new AttributeValue(record.getName())).withExists(true)
                    )));
        } catch (AmazonDynamoDBException e) {
            log.info(e.getMessage());
            log.info(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    @Override
    public VideoGameRecord findByName(String name) {
        return mapper.load(VideoGameRecord.class, name);
    }
//    VideoGameRecord record = new VideoGameRecord();
//        record.setName(name);
//
//        DynamoDBQueryExpression<VideoGameRecord> queryExpression = new DynamoDBQueryExpression<VideoGameRecord>()
//                .withHashKeyValues(record)
//                .withIndexName("name")
//                .withConsistentRead(false);
//
//        return mapper.query(VideoGameRecord.class, queryExpression);

    @Override
    public List<VideoGameRecord> getAllGames() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(VideoGameRecord.class, scanExpression);
    }

    @Override
    public VideoGameRecord updateVideoGame(VideoGameRecord record) {
        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "name",
                            new ExpectedAttributeValue().withValue(new AttributeValue(record.getName())).withExists(true)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("Game does not exist");
        }
        return record;
    }


}

